package io.computerscience.android.androidotto.test.Module;

import dagger.Module;
import dagger.Provides;
import io.computerscience.android.androidotto.Activity.MainUserActivity;
import io.computerscience.android.androidotto.Fragment.SimpleFragment;
import io.computerscience.android.androidotto.Module.MainUserActivityModule;

@Module(injects = {MainUserActivity.class, SimpleFragment.class},
        complete = false,
        library = true)
public class MainUserActivityTestModule {
    @Provides String provideStringValue() {
        return "Mock String";
    }
}
