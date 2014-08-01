package io.computerscience.android.androidotto.Fragment;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import dagger.Module;
import hugo.weaving.DebugLog;
import io.computerscience.android.androidotto.Interface.DaggerInjector;
import io.computerscience.android.androidotto.Module.SingletonModule;
import io.computerscience.android.androidotto.R;

public class SimpleFragment extends Fragment {
    private static int instace = 0;
    private int fragNum;
    private static String TAG = SimpleFragment.class.getSimpleName();
    private int lastBroadcastValue;

    @InjectView(R.id.simpleTextField) TextView simpleTextField;
    @InjectView(R.id.fragmentButton)  Button   fragmentButton;

    @Inject Bus eventBus;

    @Module(injects = {SimpleFragment.class}, includes = SingletonModule.class)
    public static class SimpleFragmentModule {
        // Define any dependency providers in this class.
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((DaggerInjector) activity.getApplication()).inject(this); // Dagger Injection
        Log.e(TAG, "Event Bus Object"+ eventBus.toString() + eventBus.hashCode());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_user, container, false);
        ButterKnife.inject(this, rootView);
        instace++;
        fragNum = instace;
        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "Register Subscriber: " + SimpleFragment.class.getSimpleName());
        eventBus.register(this); // Register as a subscriber
    }


    @Override
    public void onPause() {
        Log.d(TAG, "Unregister Subscriber: "+ SimpleFragment.class.getSimpleName());
        eventBus.unregister(this); // Unregister as we probaly will not need to update if not visible.
        super.onPause();
    }


    @Override
    public void onDestroyView() {
        ButterKnife.reset(this);
        super.onDestroyView();
    }


    /**
     * On Click Listener for R.id.fragmentButton
     * Note: the parameter is optional
     */
    @DebugLog
    @OnClick(R.id.fragmentButton)
    public void buttonIsClicked(Button clickedButton) {
        Log.d(TAG, "Text View Clicked!");
        lastBroadcastValue = fragNum;
        eventBus.post(new ButtonClickedEvent(this, fragNum));
    }


    //region Event Produces and Subscribers
    /**
     * Event Listener for ButtonClickedEvent.
     * @param event
     */
    @Subscribe
    public void onEventAvailable(ButtonClickedEvent event) {
        Log.d(TAG, "Recieve Event: " + event.getValue());
        if (!event.getSource().equals(this)) {
            simpleTextField.setText("Value Recieved: " + event.getValue());
        }
    }


    /**
     * A sample event type used in the sample application. Used to denote that a button click happened.
     */
    public static class ButtonClickedEvent {

        protected Object source;
        protected int value;

        public ButtonClickedEvent(Object src, int val){
            value = val;
            source = src;
        }

        public Object getSource() {
            return source;
        }
        public int getValue() {
            return value;
        }

    }
    //endregion

}
