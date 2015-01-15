package com.btellez.solidandroid.configuration;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * KeyValueStore interface defined common methods that
 * may be used to store a set of key's and values to
 * a more persistant storage.
 */
public interface KeyValueStore {

    public void putFloat(String key, float value);

    public float getFloat(String key, float defaultValue);

    public void putInt(String key, int value);

    public int getInt(String key, int defaultValue);

    public String getString(String key, String defaultValue);

    public String getString(String key);

    public void putString(String key, String value);
    
    public boolean getBool(String key);
    
    public void putBool(String key, boolean value);


    /**
     * No-Op implementation of KeyValue Store.
     *
     * This allows us to selectively override some and not all methods.
     * This allows us to use this instead as to satisfy dependencies in
     * tests where we are not relying on the behaviour of KeyValueStore.
     */
    public class SimpleKeyValueStore implements KeyValueStore {
        @Override public void putFloat(String key, float value) {}
        @Override public float getFloat(String key, float defaultValue) {return 0;}
        @Override public void putInt(String key, int value) {}
        @Override public int getInt(String key, int defaultValue) {return 0;}
        @Override public String getString(String key, String defaultValue) {return null;}
        @Override public String getString(String key) {return null;}
        @Override public void putString(String key, String value) {}
        @Override public boolean getBool(String key) {return false;}
        @Override public void putBool(String key, boolean value) {}
    }

    /**
     * Shared Preferences implementation of the key value store.
     * The values stored in with this key-value store implementation
     * will persist as long as the application is not unisntalled.
     */
    public static class SharedPrefs implements KeyValueStore {
        private static final String SHARE_PREFS_KEY_VALUE_STORE = "share_prefs_key_value_store";
        private SharedPreferences prefs;

        public SharedPrefs(Context context) {
            this(context, SHARE_PREFS_KEY_VALUE_STORE);
        }

        public SharedPrefs(Context context, String storeIdentifier) {
            prefs = context.getSharedPreferences(storeIdentifier, Context.MODE_PRIVATE);
        }

        @Override
        public void putFloat(String key, float value) {
            prefs.edit().putFloat(key, value).apply();
        }

        @Override
        public float getFloat(String key, float defaultValue) {
            return prefs.getFloat(key, defaultValue);
        }

        @Override
        public void putInt(String key, int value) {
            prefs.edit().putInt(key, value).apply();
        }

        @Override
        public int getInt(String key, int defaultValue) {
            return prefs.getInt(key, defaultValue);
        }

        @Override
        public void putString(String key, String value) {
            prefs.edit().putString(key, value).apply();
        }

        @Override
        public boolean getBool(String key) {
            return prefs.getBoolean(key, false);
        }

        @Override
        public void putBool(String key, boolean value) {
            prefs.edit().putBoolean(key, value).apply();
        }

        @Override
        public String getString(String key) {
            return getString(key, null);
        }

        @Override
        public String getString(String key, String defaultValue) {
            return prefs.getString(key, defaultValue);
        }
    }

    /**
     * Session is an in-memory key-value store whose data will
     * cease to exist when the application is killed.
     */
    public static class Session implements KeyValueStore {
        private HashMap<String, Object> map = new HashMap<String, Object>();

        @Override
        public void putFloat(String key, float value) {
            map.put(key, value);
        }

        @Override
        public float getFloat(String key, float defaultValue) {
            return map.containsKey(key) ? (Float) map.get(key) : defaultValue;
        }

        @Override
        public void putInt(String key, int value) {
            map.put(key, value);
        }

        @Override
        public int getInt(String key, int defaultValue) {
            return map.containsKey(key) ? (Integer) map.get(key) : defaultValue;
        }

        @Override
        public String getString(String key) {
            return getString(key, null);
        }

        @Override
        public String getString(String key, String defaultValue) {
            return map.containsKey(key) ? (String) map.get(key) : defaultValue;
        }

        @Override
        public void putString(String key, String value) {
            map.put(key, value);
        }

        @Override
        public boolean getBool(String key) {
            return map.containsKey(key) && (boolean) map.get(key);
        }

        @Override
        public void putBool(String key, boolean value) {
            map.put(key, value);
        }
    }
}
