package com.btellez.solidandroid.module;

import android.content.Context;

import dagger.ObjectGraph;

/**
 * This interface defines behaviour that must be implemented by any
 * class that is to act as an injector. Ideally it should be a class
 * that will be a singleton instance and global in scope. Extensions
 * of `Application` are ideal for this.
 */
public interface DependencyInjector {
    public Object[] getModules();
    public void inject(Object o);


    public static class SolidInjector implements DependencyInjector {
        private ObjectGraph mObjectGraph;
        private Context context;

        public SolidInjector(Context context) {
            this.context = context;
        }

        @Override
        public Object[] getModules() {
            return new Object[]{
                    // Each Activity and Class Defined their own Injector,
                    // Shared Modules should go under the ../Module package
            };
        }

        @Override
        public void inject(Object o) {
            mObjectGraph.inject(o);
        }
    }
}
