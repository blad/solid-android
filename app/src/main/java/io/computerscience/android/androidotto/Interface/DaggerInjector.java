package io.computerscience.android.androidotto.Interface;

/**
 * This interface defines behaviour that must be implemented by any
 * class that is to act as an injector. Ideally it should be a class
 * that will be a singleton instance and global in scope. Extensions
 * of `Application` are ideal for this.
 */
public interface DaggerInjector {
    /**
     * Return the list of modules that are defined for injecting
     * @return
     */
    public Object[] getModules();

    /**
     * This method takes a class and injects the dependencies according
     * to the modules defined in the #getModules method.
     * @param o
     */
    public void inject(Object o);
}
