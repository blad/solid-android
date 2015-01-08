package com.btellez.solidandroid.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.btellez.solidandroid.R;
import com.btellez.solidandroid.configuration.Configuration;
import com.btellez.solidandroid.model.Icon;
import com.btellez.solidandroid.module.DependencyInjector;
import com.btellez.solidandroid.module.SingletonModule;
import com.btellez.solidandroid.network.NetworkBitmapClient;
import com.btellez.solidandroid.network.NounProjectApi;
import com.btellez.solidandroid.utility.Strings;
import com.btellez.solidandroid.view.EmptyView;
import com.btellez.solidandroid.view.SingleIconResultItem;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.MalformedJsonException;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import dagger.Module;

public class SearchResultsActivity extends FragmentActivity {

    private static final String EXTRA_QUERY_STRING = "extra_query_string";
    private static final String ACTION_DISPLAY_SEARCH_RESULTS = "action_display_search_results";
    private static final String ACTION_DISPLAY_RECENT_UPLOADS = "action_display_recent_uploads";

    @Inject NetworkBitmapClient networkBitmap;
    @Inject NounProjectApi nounProjectApi;
    @Inject Configuration configuration;
    @InjectView(R.id.result_list) ListView listView;
    @InjectView(R.id.progress_indicator) ProgressBar progress;

    protected List<Icon> data;
    protected BaseAdapter adapter;
    protected EmptyView emptyView;
    protected SingleIconResultItem.Listener itemListener;

    private enum State {Loading, Error, Empty, Loaded}
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        FrameLayout content = ((FrameLayout) findViewById(android.R.id.content));
        ButterKnife.inject(this);
        ((DependencyInjector) getApplication()).inject(this);
        
        emptyView = new EmptyView(this);
        emptyView.setVisibility(View.INVISIBLE);
        content.addView(emptyView);

        itemListener = new ViewListener();
        adapter = new IconListAdapter();
        
        listView.setVisibility(View.INVISIBLE);
        listView.setAdapter(adapter);
        listView.setEmptyView(emptyView);
        
        setState(State.Loading);
    }
    
    private void setState(State state) {
        switch (state) {
            case Loading:
                executeIntentAction();
                emptyView.setVisibility(View.INVISIBLE);
                listView.setVisibility(View.INVISIBLE);
                progress.setVisibility(View.VISIBLE);
                break;
            case Error:
                emptyView.setVisibility(View.VISIBLE);
                listView.setVisibility(View.INVISIBLE);
                progress.setVisibility(View.INVISIBLE);
                onErrorState();
                break;
            case Empty:
                emptyView.setVisibility(View.VISIBLE);
                listView.setVisibility(View.INVISIBLE);
                progress.setVisibility(View.INVISIBLE);
                onEmptyState();
                break;
            case Loaded:
                emptyView.setVisibility(View.INVISIBLE);
                listView.setVisibility(View.VISIBLE);
                progress.setVisibility(View.INVISIBLE);
                break;
        }
    }
    
    private void executeIntentAction() {
        if (ACTION_DISPLAY_RECENT_UPLOADS.equals(getIntent().getAction())) {
            nounProjectApi.recent(new ApiCallback());
            setTitle(R.string.recent_uploads);
        } else {
            String query = getIntent().getStringExtra(EXTRA_QUERY_STRING);
            nounProjectApi.search(query, new ApiCallback());
            setTitle(getString(R.string.query_pattern, query));
        }
    }

    private void onEmptyState() {
        emptyView.setHeadline(R.string.no_results);
        emptyView.setSubheadline(R.string.no_results_detail);
        emptyView.setPrimaryActionName(R.string.change_search);
        emptyView.setSecondaryActionName(R.string.go_back);
        emptyView.setListener(new EmptyView.Listener() {
            @Override public void onPrimaryActionClicked() {
                finish();
            }
            @Override public void onSecondaryActionClicked() {
                finish();
            }
        });
    }

    private void onErrorState() {
        emptyView.setHeadline(R.string.unable_to_load);
        emptyView.setSubheadline(R.string.unable_to_load_detail);
        emptyView.setPrimaryActionName(R.string.try_again);
        emptyView.setSecondaryActionName(R.string.go_back);
        emptyView.setListener(new EmptyView.Listener() {
            @Override public void onPrimaryActionClicked() {
                setState(State.Loading);
            }
            @Override public void onSecondaryActionClicked() {
                finish();
            }
        });
    }

    public static class Builder {
        String query;
        Context context;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder withSearchTerm(String query) {
            this.query = query;
            return this;
        }

        public Intent build() {
            Intent intent = new Intent(context, SearchResultsActivity.class);
            if (!Strings.isEmpty(query)) {
                intent.setAction(ACTION_DISPLAY_SEARCH_RESULTS);
                intent.putExtra(EXTRA_QUERY_STRING, query);
            } else {
                intent.setAction(ACTION_DISPLAY_RECENT_UPLOADS);
            }
            return intent;
        }
    }

    private class ViewListener implements SingleIconResultItem.Listener {
        @Override
        public void onLinkClicked(String path) {
            String finalUrl = configuration.getNounProjectBaseUrl() + path;
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(finalUrl));
            startActivity(intent);
        }

        @Override
        public void requestDownloadInto(String url, ImageView imageView) {
            networkBitmap.downloadInto(url, imageView);
        }
    }

    private class IconListAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            if (data == null)
                return 0;
            return data.size();
        }
        @Override
        public Object getItem(int position) {
            return data.get(position);
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public boolean areAllItemsEnabled() {
            return false;
        }
        @Override
        public boolean isEnabled(int position) {
            return false;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            SingleIconResultItem listItem = (SingleIconResultItem) convertView;
            if (listItem == null) {
                listItem = new SingleIconResultItem(SearchResultsActivity.this);
                listItem.setListener(itemListener);
            }
            listItem.setIconData((Icon) getItem(position));
            return listItem;
        }
    }

    private class ApiCallback implements NounProjectApi.Callback {
        @Override
        public void onSuccess(List<Icon> icons) {
            data = icons;
            adapter.notifyDataSetChanged();
            setState(State.Loaded);
        }

        @Override
        public void onFailure(Throwable error) {
            boolean isSearch = !ACTION_DISPLAY_RECENT_UPLOADS.equals(getIntent().getAction());
            boolean isNoResultJson = error instanceof JsonSyntaxException;
            if (isSearch && isNoResultJson) {
                setState(State.Empty);
            } else {
                setState(State.Error);
            }
            adapter.notifyDataSetChanged();
        }
    }

    @Module(injects = {SearchResultsActivity.class}, includes = SingletonModule.class)
    public static class SearchResultDepedencyModule { }
}
