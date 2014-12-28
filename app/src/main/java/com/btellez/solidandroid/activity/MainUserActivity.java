package com.btellez.solidandroid.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.squareup.otto.Bus;
import com.squareup.otto.Produce;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

import dagger.Module;
import dagger.Provides;
import com.btellez.solidandroid.fragment.SimpleFragment;
import com.btellez.solidandroid.module.DaggerInjector;
import com.btellez.solidandroid.module.SingletonModule;
import com.btellez.solidandroid.network.SimpleApi;
import com.btellez.solidandroid.R;


public class MainUserActivity extends FragmentActivity {

    private static String TAG = MainUserActivity.class.getSimpleName();
    private int recentValue;
    private Object recentSource;

    @Inject Bus       eventBus;
    @Inject String    sampleString;
    @Inject SimpleApi api;

    //region Module that injects this class and components used by this class
    @Module(injects = {MainUserActivity.class, SimpleApi.class}, includes = SingletonModule.class)
    public static class MainUserActivityModule {
        // Sample Provider for this class:
        @Provides String provideStringValue() {
            return "Hello World";
        }
    }
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((DaggerInjector) getApplication()).inject(this); // Dagger Injection

        Log.e(TAG, "Event Bus Object" + eventBus.toString() + eventBus.hashCode());
        setContentView(R.layout.activity_main_user);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentA, new SimpleFragment())
                    .commit();

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentB, new SimpleFragment())
                    .commit();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        // Register as a Producer
        eventBus.register(this);
        postEvent(new SimpleFragment.ButtonClickedEvent(this, 100));
        api.getAllData();
    }


    @Override
    protected void onPause() {
        super.onPause();
        // Unregister as a Producer
        eventBus.unregister(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_user, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        postEvent(new SimpleFragment.ButtonClickedEvent(this, id));

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //region Event Producers and Subscribers
    /**
     * Anytime any new Broadcaster are registered, We are the Authority on it the
     * last value that was registered for this type of event.
     * @return
     */
    @Produce
    public SimpleFragment.ButtonClickedEvent getRecentValue() {
        return new SimpleFragment.ButtonClickedEvent(recentSource, recentValue);
    }


    /**
     * Listen for events from other broadcasters.
     *
     * @param event
     */
    @Subscribe
    public void updateState(SimpleFragment.ButtonClickedEvent event) {
        recentValue = event.getValue();
    }


    @Subscribe
    public void onNetworkSuccess(SimpleApi.Success event) {
        Log.d(TAG, "Network Success Received");
        Log.d(TAG, "Network Data:" + event.getResult().toString());
        // Do Something With network event
    }


    @Subscribe
    public void onNetworkFailure(SimpleApi.Failure event) {
        Log.d(TAG, "Network Failure Received");
        Log.d(TAG, "Network Exception:"+ event.getResult().toString());
        // Do something with network event
    }
    //endregion

    /**
     * Helper method for posting an event, and rem
     * @param event
     */
    protected void postEvent(SimpleFragment.ButtonClickedEvent event) {
        Log.d(TAG, "Posting Event");
        recentSource = ((SimpleFragment.ButtonClickedEvent) event).getSource();
        recentValue = ((SimpleFragment.ButtonClickedEvent)event).getValue();
        eventBus.post(event);
    }

    public String getSampleString() {
        return sampleString;
    }
}
