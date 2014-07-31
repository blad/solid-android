package io.computerscience.android.androidotto.test.Application;

import android.app.Application;
import android.content.Context;

import dagger.ObjectGraph;
import io.computerscience.android.androidotto.Interface.DaggerInjector;

/**
 * This class defined standard behaviour that DaggerInjector needs, and provides an
 * application context for tests
 *
 * Define the getModules() method in an anonymous inner-class implementation defined
 * within each Test, since not all modules will be required for all tests.
 */
public abstract class MockApplicationInjectable extends Application implements DaggerInjector {
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
