package com.btellez.solidandroid.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.btellez.solidandroid.R;
import com.btellez.solidandroid.module.DependencyInjector;
import com.btellez.solidandroid.module.SingletonModule;
import com.btellez.solidandroid.utility.Tracker;
import com.btellez.solidandroid.view.SelectionScreenView;

import javax.inject.Inject;

import dagger.Module;

public class SelectionActivity extends FragmentActivity {

    private SelectionScreenView selectionScreenView;
    @Inject Tracker tracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((DependencyInjector) getApplication()).inject(this);
        selectionScreenView = new SelectionScreenView(this);
        setContentView(selectionScreenView);
        selectionScreenView.setListener(new SelectionScreenView.SimpleListener() {
            @Override
            public void onRecentGroupSelected() {
                Intent intent = new SearchResultsActivity
                                        .Builder(SelectionActivity.this)
                                        .build();
                startActivity(intent);
                tracker.track("Recent Selected");
            }

            @Override
            public void onSubmitSearchQuery(String query) {
                Intent intent = new SearchResultsActivity
                                        .Builder(SelectionActivity.this)
                                        .withSearchTerm(query).build();
                startActivity(intent);
                tracker.track("Search Submitted", "query", query);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_user, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Module(injects = {SelectionActivity.class}, includes = SingletonModule.class)
    public static class SelectionActivityDepedencyModule { }
}
