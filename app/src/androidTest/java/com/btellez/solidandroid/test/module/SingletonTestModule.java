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


    @Provides @Singleton SimpleApi provideSimpleApi(Bus bus, Context context) {
        // We return a spy rather than a mock because the SimpleApi class is injected by Dagger
        // and returns a SimpleApi_Proxy, which is triggers an Exception since it is not explicitly
        // registered for injections.
        return Mockito.spy(new SimpleApi(bus, context));
    }
}
