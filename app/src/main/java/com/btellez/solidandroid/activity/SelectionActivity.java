package com.btellez.solidandroid.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.btellez.solidandroid.R;
import com.btellez.solidandroid.view.SelectionScreenView;

public class SelectionActivity extends FragmentActivity {

    private SelectionScreenView selectionScreenView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        selectionScreenView = new SelectionScreenView(this);
        setContentView(selectionScreenView);
        selectionScreenView.setListener(new SelectionScreenView.SimpleListener() {
            @Override
            public void onRecentGroupSelected() {
                Intent intent = new SearchResultsActivity
                                        .Builder(SelectionActivity.this)
                                        .build();
                startActivity(intent);
            }

            @Override
            public void onSubmitSearchQuery(String query) {
                Intent intent = new SearchResultsActivity
                                        .Builder(SelectionActivity.this)
                                        .withSearchTerm(query).build();
                startActivity(intent);
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
}
