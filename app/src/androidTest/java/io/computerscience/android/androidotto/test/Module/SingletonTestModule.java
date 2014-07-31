package io.computerscience.android.androidotto.test.Module;

import android.content.Context;

import com.squareup.otto.Bus;

import org.mockito.Mockito;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.computerscience.android.androidotto.Activity.MainUserActivity;
import io.computerscience.android.androidotto.Fragment.SimpleFragment;
import io.computerscience.android.androidotto.SimpleAndroidApplication;

@Module(injects = {MainUserActivity.class, SimpleFragment.class},
        library = true,
        complete = false)
public class SingletonTestModule {

    private Context application;

    public SingletonTestModule(Context app) {
        application = app;
    }

    @Provides @Singleton Context provideApplicationContext() {
        return Mockito.mock(SimpleAndroidApplication.class);
    }

    @Provides @Singleton Bus provideEventBus() {
        return Mockito.mock(Bus.class);
    }
}
