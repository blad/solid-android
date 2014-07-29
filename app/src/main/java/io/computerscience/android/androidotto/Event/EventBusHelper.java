package io.computerscience.android.androidotto.Event;


import com.squareup.otto.Bus;

public class EventBusHelper {

    protected static Bus mInstance = null;

    protected EventBusHelper() {}

    public static Bus getInstance() {
        if (mInstance == null)
            mInstance = new Bus();
        return mInstance;
    }
}
