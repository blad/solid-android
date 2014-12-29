package com.btellez.solidandroid.activity;

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
import com.btellez.solidandroid.view.SingleIconResultItem;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import dagger.Module;

public class SearchResultsActivity extends FragmentActivity {

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
        nounProjectApi.recent(new ApiCallback());
        itemListener = new ViewListener();
        adapter = new IconListAdapter();
        listView.setAdapter(adapter);
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
