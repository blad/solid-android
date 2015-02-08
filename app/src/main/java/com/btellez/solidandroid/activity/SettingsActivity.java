package com.btellez.solidandroid.activity;

import android.app.Activity;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.btellez.solidandroid.R;
import com.btellez.solidandroid.module.DependencyInjector;
import com.btellez.solidandroid.module.SingletonModule;
import com.btellez.solidandroid.utility.Tracker;

import javax.inject.Inject;

import dagger.Module;

public class SettingsActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, new GeneralPreferenceFragment())
                .commit();
    }

    public static class GeneralPreferenceFragment extends PreferenceFragment {

        @Inject Tracker tracker;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general);

            findPreference(getString(R.string.pref_key_enable_history))
                    .setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                        @Override
                        public boolean onPreferenceClick(Preference preference) {
                            tracker.track("Toggled History", "from", preference.getSummary().toString());
                            // TODO: Clear History.
                            return true;
                        }
                    });
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((DependencyInjector) activity.getApplication()).inject(this);
        }

        @Module(injects = {GeneralPreferenceFragment.class}, includes = SingletonModule.class)
        public static class GeneralPreferenceDepedencyModule { }
    }
}
