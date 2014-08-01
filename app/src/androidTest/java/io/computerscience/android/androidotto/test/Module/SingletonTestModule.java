package io.computerscience.android.androidotto.test.Module;

import com.squareup.otto.Bus;

import org.mockito.Mockito;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.computerscience.android.androidotto.Network.SimpleApi;

/**
 * This class provides mock instances of the singletons so that
 * behaviours can be verified using Mockito during tests.
 */
@Module(library = true)
public class SingletonTestModule {

    @Provides @Singleton Bus provideEventBus() {
        return Mockito.mock(Bus.class);
    }


    @Provides @Singleton SimpleApi provideSimpleApi(Bus bus) {
        return Mockito.spy(new SimpleApi(bus));
    }
}
