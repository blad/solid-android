package com.btellez.solidandroid;

import android.app.Application;
import android.content.Context;

import dagger.Module;
import dagger.ObjectGraph;
import dagger.Provides;
import com.btellez.solidandroid.activity.MainUserActivity;
import com.btellez.solidandroid.fragment.SimpleFragment;
import com.btellez.solidandroid.module.DaggerInjector;

public class SimpleAndroidApplication extends Application implements DaggerInjector {

    //region Application Context Module
    @Module(library = true)
    public final class ApplicationContextModule {
        Context context;
        public ApplicationContextModule(Context app) {
            context = app;
        }

        @Provides public Context provideContext(){
            return context;
        }
    }
    //endregion

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
                new SimpleFragment.SimpleFragmentModule(),
                new ApplicationContextModule(this)
        };
    }


    @Override
    public void inject(Object o) {
        mObjectGraph.inject(o);
    }

}
