package com.btellez.solidandroid.module;

import android.content.Context;

import com.btellez.solidandroid.BuildConfig;
import com.btellez.solidandroid.configuration.Configuration;
import com.btellez.solidandroid.model.IconParser;
import com.btellez.solidandroid.network.NetworkBitmapClient;
import com.btellez.solidandroid.network.NounProjectApi;
import com.btellez.solidandroid.network.OkNounProjectApi;
import com.btellez.solidandroid.utility.Tracker;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import com.btellez.solidandroid.network.SimpleApi;
import com.btellez.solidandroid.SimpleAndroidApplication;

/**
 * This module defines is a provider for singletons across the application.
 *
 * If you find yourself creating a singleton class, you may be able to simply
 * use the @Singleton annotation and inject the singleton into your activities
 * or fragments.
 *
 * This module is intended to be extended. Use `includes = SingletonModule.class`
 * to include the injections defined here.
 *
 * including the ApplicationContextModule allows us to resolve the Context dependencies
 * automatically. Ensure that a ApplicationContextModule is initialize and added to the
 * list of modules being initialized.
 */
@Module(library = true, includes = SimpleAndroidApplication.ApplicationContextModule.class)
public class SingletonModule {

    @Provides @Singleton Configuration provideConfiguration(Context context) {
        return new Configuration.DevelopmentConfiguration(context);
    }

    @Provides @Singleton IconParser provudeIconParser() {
        return new IconParser.GsonIconParser();
    }

    @Provides @Singleton NounProjectApi provideNounProjectApi(Configuration configuration, IconParser parser) {
        return new OkNounProjectApi(configuration, parser);
    }

    @Provides @Singleton NetworkBitmapClient provideNetworkBitmapClient() {
        return new NetworkBitmapClient.PicassoBitmapClient();
    }

    @Provides @Singleton Bus provideEventBus() {
        return new Bus();
    }

    @Provides @Singleton SimpleApi provideSimpleApi(Bus bus, Context context) {
        return new SimpleApi(bus, context);
    }

    @Provides @Singleton Tracker provideTracker(Context context, Configuration appConfig) {
        if (BuildConfig.DEBUG) {
            return new Tracker.SimpleTracker();
        } else {
            return new Tracker.ReplayTracker(context, appConfig);
        }
    }
}
