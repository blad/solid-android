package io.computerscience.android.androidotto.test.Application;

import android.app.Application;
import android.content.Context;

import dagger.ObjectGraph;
import io.computerscience.android.androidotto.Interface.Injectable;
import io.computerscience.android.androidotto.Module.SingletonModule;
import io.computerscience.android.androidotto.SimpleAndroidApplication;
import io.computerscience.android.androidotto.test.Module.MainUserActivityTestModule;
import io.computerscience.android.androidotto.test.Module.SingletonTestModule;

/**
 * Created by tellez on 7/30/14.
 */
public abstract class MockApplicationInjectable extends Application implements Injectable {
    private ObjectGraph mObjectGraph;

    public MockApplicationInjectable(Context context) {
        super.attachBaseContext(context);
        mObjectGraph = ObjectGraph.create(getModules());
    }

    @Override
    public void inject(Object o) {
        mObjectGraph.inject(o);
    }
}
