package com.btellez.solidandroid.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.btellez.solidandroid.R;
import com.btellez.solidandroid.configuration.Configuration;
import com.btellez.solidandroid.model.Icon;
import com.btellez.solidandroid.module.DependencyInjector;
import com.btellez.solidandroid.module.SingletonModule;
import com.btellez.solidandroid.network.NetworkBitmapClient;
import com.btellez.solidandroid.network.NounProjectApi;
import com.btellez.solidandroid.utility.Strings;
import com.btellez.solidandroid.view.SingleIconResultItem;

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

    protected List<Icon> data;
    protected BaseAdapter adapter;
    protected SingleIconResultItem.Listener itemListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        ButterKnife.inject(this);
        ((DependencyInjector) getApplication()).inject(this);

        // Configure the activity

        if (ACTION_DISPLAY_RECENT_UPLOADS.equals(getIntent().getAction())) {
            nounProjectApi.recent(new ApiCallback());
            setTitle(R.string.recent_uploads);
        } else {
            String query = getIntent().getStringExtra(EXTRA_QUERY_STRING);
            nounProjectApi.search(query, new ApiCallback());
            setTitle(getString(R.string.query_pattern, query));
        }

        itemListener = new ViewListener();
        adapter = new IconListAdapter();
        listView.setAdapter(adapter);
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
        }

        @Override
        public void onFailure(Throwable error) {
            Toast.makeText(SearchResultsActivity.this, "Failure Occurred Unable to Download form API", Toast.LENGTH_SHORT).show();
        }
    }

    @Module(injects = {SearchResultsActivity.class}, includes = SingletonModule.class)
    public static class SearchResultDepedencyModule { }
}
