package io.computerscience.android.androidotto.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import io.computerscience.android.androidotto.Event.EventBusHelper;
import io.computerscience.android.androidotto.Event.Type.ButtonClickedEvent;
import io.computerscience.android.androidotto.R;

public class SimpleFragment extends Fragment {
    private static int instace = 0;
    private int fragNum;
    private static String TAG = SimpleFragment.class.getSimpleName();
    private int lastBroadcastValue;
    @InjectView(R.id.simpleTextField) TextView simpleTextField;
    @InjectView(R.id.fragmentButton)  Button   fragmentButton;


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
        EventBusHelper.getInstance().register(this); // Register as a subscriber
    }


    @Override
    public void onPause() {
        Log.d(TAG, "Unregister Subscriber: "+ SimpleFragment.class.getSimpleName());
        EventBusHelper.getInstance().unregister(this); // Unregister as we probaly will not need to update if not visible.
        super.onPause();
    }


    @Override
    public void onDestroyView() {
        ButterKnife.reset(this);
        super.onDestroyView();
    }


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
     * On Click Listener for R.id.fragmentButton
     */
    @OnClick(R.id.fragmentButton)
    public void buttonIsClicked() {
        Log.d(TAG, "Text View Clicked!");
        lastBroadcastValue = fragNum;
        EventBusHelper.getInstance().post(new ButtonClickedEvent(this, fragNum));
    }

}
