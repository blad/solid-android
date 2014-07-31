package io.computerscience.android.androidotto;

import android.app.Application;
import android.content.Context;

import dagger.ObjectGraph;
import io.computerscience.android.androidotto.Interface.Injectable;
import io.computerscience.android.androidotto.Module.Modules;

public class SimpleAndroidApplication extends Application implements Injectable {
    ObjectGraph graph;

    private ObjectGraph mObjectGraph;

    @Override
    public void onCreate() {
        super.onCreate();
        mObjectGraph = ObjectGraph.create(getModules());
    }

    @Override
    public Object[] getModules() {
        return Modules.list(getApplicationContext());
    }

    @Override
    public void inject(Object o) {
        mObjectGraph.inject(o);
    }

    public static Injectable getInjectable(Context context) {
        return (Injectable) context.getApplicationContext();
    }

}
