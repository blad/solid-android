package com.btellez.solidandroid.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

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
                // TODO: Start Recent Group Activity
            }

            @Override
            public void onSubmitSearchQuery(String query) {
                // TODO: Start Search Results Activity
            }
        });
    }
}
