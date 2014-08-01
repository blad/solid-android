package io.computerscience.android.androidotto.Module;

import android.content.Context;

import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.computerscience.android.androidotto.Activity.MainUserActivity;
import io.computerscience.android.androidotto.Fragment.SimpleFragment;
import io.computerscience.android.androidotto.Network.SimpleApi;

/**
 * This module defines is a provider for singletons across the application.
 *
 * If you find yourself creating a singleton class, you may be able to simply
 * use the @Singleton annotation and inject the singleton into your activities
 * or fragments.
 *
 * This module is intended to be extended. Use `includes = SingletonModule.class`
 * to include the injections defined here.
 */
@Module(library = true)
public class SingletonModule {

    @Provides @Singleton Bus provideEventBus() {
        return new Bus();
    }

    @Provides @Singleton SimpleApi provideSimpleApi() {
        return new SimpleApi();
    }
}
