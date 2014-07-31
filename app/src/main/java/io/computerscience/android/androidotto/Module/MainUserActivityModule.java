package io.computerscience.android.androidotto.Module;

import android.app.Application;
import android.content.Context;

import dagger.Module;
import dagger.Provides;
import io.computerscience.android.androidotto.Activity.MainUserActivity;
import io.computerscience.android.androidotto.Fragment.SimpleFragment;

// `complete` = {true|false}
// Incomplete modules are permitted to have missing dependencies.
//
// `library` = {true|false}
// If your module's bindings will be used outside of the listed injects then mark the module
// as a library.
//
// `includes` = Class<?>
// Used to extend a module's behavious.
@Module (injects = {MainUserActivity.class},
         complete = false,
         library = true)
public class MainUserActivityModule {
    // Add Injectors
    @Provides String provideStringValue() {
        return "Hello World";
    }
}
