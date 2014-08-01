package io.computerscience.android.androidotto.Activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.JsonObject;
import com.squareup.otto.Bus;
import com.squareup.otto.Produce;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

import dagger.Module;
import dagger.Provides;
import io.computerscience.android.androidotto.Fragment.SimpleFragment;
import io.computerscience.android.androidotto.Interface.DaggerInjector;
import io.computerscience.android.androidotto.Module.SingletonModule;
import io.computerscience.android.androidotto.Network.SimpleApi;
import io.computerscience.android.androidotto.R;


public class MainUserActivity extends FragmentActivity {

    private static String TAG = MainUserActivity.class.getSimpleName();
    private int recentValue;
    private Object recentSource;

    @Inject Bus       eventBus;
    @Inject String    sampleString;
    @Inject SimpleApi api;

    @Module(injects = MainUserActivity.class, includes = SingletonModule.class)
    public static class MainUserActivityModule {
        // Sample Provider:
        @Provides String provideStringValue() {
            return "Hello World";
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((DaggerInjector) getApplication()).inject(this); // Dagger Injection
        Log.e(TAG, "Event Bus Object"+ eventBus.toString() + eventBus.hashCode());
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
        api.getAllData(this, new SimpleApi.NetworkCallback() {
            @Override
            public void onSuccess(JsonObject value) {
                Log.e("Success!", value.toString());
            }

            @Override
            public void onFailure(Exception exception) {
                Log.e("Success!", exception.getStackTrace().toString());
            }
        });
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

    public Bus getEventBus() {
        return eventBus;
    }

    public String getSampleString() {
        return sampleString;
    }
}
