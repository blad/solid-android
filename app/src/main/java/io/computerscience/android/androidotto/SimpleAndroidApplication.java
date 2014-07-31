package io.computerscience.android.androidotto;

import android.app.Application;
import android.content.Context;

import dagger.ObjectGraph;
import io.computerscience.android.androidotto.Activity.MainUserActivity;
import io.computerscience.android.androidotto.Fragment.SimpleFragment;
import io.computerscience.android.androidotto.Interface.DaggerInjector;

public class SimpleAndroidApplication extends Application implements DaggerInjector {

    private ObjectGraph mObjectGraph;

    @Override
    public void onCreate() {
        super.onCreate();
        mObjectGraph = ObjectGraph.create(getModules());
    }


    @Override
    public Object[] getModules() {
        return new Object[]{
                // Each Activity and Class Defined their own Injector,
                // Shared Modules should go under the ../Module package
                new MainUserActivity.MainUserActivityModule(),
                new SimpleFragment.SimpleFragmentModule()
        };
    }


    @Override
    public void inject(Object o) {
        mObjectGraph.inject(o);
    }

}
