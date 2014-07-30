package io.computerscience.android.androidotto.Module;

import android.content.Context;

import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.computerscience.android.androidotto.Activity.MainUserActivity;
import io.computerscience.android.androidotto.Event.EventBusHelper;

// `complete` = {true|false}
// Incomplete modules are permitted to have missing dependencies.
//
// `library` = {true|false}
// If your module's bindings will be used outside of the listed injects then mark the module
// as a library.
//
@Module (injects = MainUserActivity.class,
        complete = false)
public class MainUserActivityModule {
    private final Context application;

    public MainUserActivityModule(Context ctx) {
        application = ctx;
    }

    @Provides @Singleton Context provideApplicationContext() {
        return application;
    }
    @Provides @Singleton Bus provideEventBus() {
        return EventBusHelper.getInstance();
    }
}
