package io.computerscience.android.androidotto.Module;

import android.content.Context;

import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.computerscience.android.androidotto.Activity.MainUserActivity;
import io.computerscience.android.androidotto.Fragment.SimpleFragment;

@Module(injects = {MainUserActivity.class, SimpleFragment.class},
        complete = false,
        library = true)
public class SingletonModule {
    protected Context application;

    public SingletonModule(Context ctx) {
        application = ctx;
    }

    @Provides @Singleton Context provideApplicationContext() {
        return application;
    }
    @Provides @Singleton Bus provideEventBus() {
        return new Bus();
    }
}
