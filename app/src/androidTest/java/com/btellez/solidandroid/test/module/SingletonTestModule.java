package com.btellez.solidandroid.test.module;

import android.content.Context;

import com.squareup.otto.Bus;

import org.mockito.Mockito;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import com.btellez.solidandroid.test.MockApplicationInjectable;

/**
 * This class provides mock instances of the singletons so that
 * behaviours can be verified using Mockito during tests.
 */
@Module(library = true, includes = MockApplicationInjectable.MockApplicationContextModule.class)
public class SingletonTestModule {

    @Provides @Singleton Bus provideEventBus() {
        return Mockito.mock(Bus.class);
    }

}
