package io.computerscience.android.androidotto;

import android.app.Application;

import dagger.ObjectGraph;
import io.computerscience.android.androidotto.Module.MainUserActivityModule;

public class SimpleAndroidApplication extends Application {
    ObjectGraph graph;

    @Override
    public void onCreate() {
        super.onCreate();
        graph = ObjectGraph.create(getModules());
    }

    protected Object[] getModules() {
        return new Object[] { new MainUserActivityModule(this) };
    }

    public void inject(Object object) {
        graph.inject(object);
    }


}
