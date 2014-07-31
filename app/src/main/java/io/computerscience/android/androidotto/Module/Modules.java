package io.computerscience.android.androidotto.Module;

import android.content.Context;

public final class Modules {

    private Modules() {}

    public static Object[] list(Context context) {
        return new Object[]{
                new MainUserActivityModule(),
                new SingletonModule(context)
        };
    }
}
