package com.btellez.solidandroid.test;

import android.app.Application;
import android.content.Context;

import dagger.Module;
import dagger.ObjectGraph;
import dagger.Provides;

import com.btellez.solidandroid.module.DependencyInjector;

/**
 * This class defined standard behaviour that DaggerInjector needs, and provides an
 * application context for tests
 *
 * Define the getModules() method in an anonymous inner-class implementation defined
 * within each Test, since not all modules will be required for all tests.
 */
public abstract class MockApplicationInjectable extends Application implements DependencyInjector {

    //region Application Context Module
    @Module(library = true)
    public final class MockApplicationContextModule {
        Context context;
        public MockApplicationContextModule(Context app) {
            context = app;
        }

        @Provides
        public Context provideContext(){
            return context;
        }
    }
    //endregion

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
