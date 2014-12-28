package com.btellez.solidandroid.module;

import android.content.Context;

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

    @Provides @Singleton Bus provideEventBus() {
        return new Bus();
    }

    // The parameter to this provider indicates that SimpleApi has a dependency on Bus.
    // This dependency will be resolved automatically by Dagger upon injection.
    @Provides @Singleton SimpleApi provideSimpleApi(Bus bus, Context context) {
        return new SimpleApi(bus, context);
    }
}
