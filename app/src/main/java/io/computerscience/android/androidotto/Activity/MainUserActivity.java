package io.computerscience.android.androidotto.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.squareup.otto.Bus;
import com.squareup.otto.Produce;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

import io.computerscience.android.androidotto.Event.Type.BusEvent;
import io.computerscience.android.androidotto.Event.Type.ButtonClickedEvent;
import io.computerscience.android.androidotto.Fragment.SimpleFragment;
import io.computerscience.android.androidotto.R;
import io.computerscience.android.androidotto.SimpleAndroidApplication;


public class MainUserActivity extends FragmentActivity {

    private static String TAG = MainUserActivity.class.getSimpleName();
    private int recentValue;
    private Object recentSource;

    @Inject Context context;
    @Inject Bus eventBus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((SimpleAndroidApplication) getApplication()).inject(this); // Dagger Injection

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
        postEvent(new ButtonClickedEvent(this, 100));
    }


    @Override
    protected void onPause() {
        // Unregister as a Producer
        eventBus.unregister(this);
        super.onPause();
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
        postEvent(new ButtonClickedEvent(this, id));

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
    public ButtonClickedEvent getRecentValue() {
        return new ButtonClickedEvent(recentSource, recentValue);
    }


    /**
     * Listen for events from other broadcasters.
     *
     * @param event
     */
    @Subscribe
    public void updateState(ButtonClickedEvent event) {
        recentValue = event.getValue();
    }


    /**
     * Helper method for posting an event, and rem
     * @param event
     */
    protected void postEvent(BusEvent event) {
        Log.d(TAG, "Posting Event");
        if (event instanceof ButtonClickedEvent) {
            recentSource = ((ButtonClickedEvent) event).getSource();
            recentValue = ((ButtonClickedEvent)event).getValue();
        }
        eventBus.post(event);
    }
}
