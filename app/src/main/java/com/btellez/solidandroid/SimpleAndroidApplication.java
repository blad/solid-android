package com.btellez.solidandroid;

import android.app.Application;
import android.content.Context;

import com.btellez.solidandroid.activity.SearchResultsActivity;
import com.btellez.solidandroid.activity.SelectionActivity;
import com.btellez.solidandroid.activity.SettingsActivity;
import com.btellez.solidandroid.module.DependencyInjector;

import dagger.Module;
import dagger.ObjectGraph;
import dagger.Provides;

public class SimpleAndroidApplication extends Application implements DependencyInjector {

    //region Application Context Module
    @Module(library = true)
    public final class ApplicationContextModule {
        Context context;
        public ApplicationContextModule(Context app) {
            context = app;
        }

        @Provides public Context provideContext() {
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
                new ApplicationContextModule(this),
                new SearchResultsActivity.SearchResultDepedencyModule(),
                new SelectionActivity.SelectionActivityDepedencyModule(),
                new SettingsActivity.GeneralPreferenceFragment.GeneralPreferenceDepedencyModule()
        };
    }


    @Override
    public void inject(Object o) {
        mObjectGraph.inject(o);
    }

}
