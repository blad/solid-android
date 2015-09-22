package com.btellez.solidandroid.utility;

import android.content.Context;

import com.btellez.solidandroid.BuildConfig;
import com.btellez.solidandroid.configuration.Configuration;

import java.util.Map;

import hugo.weaving.DebugLog;
import io.replay.framework.Config;
import io.replay.framework.ReplayIO;

public interface Tracker {
    void track(String eventName);
    void track(String eventName, String...properties);
    void track(String eventName, Map<String, ?> properties);

    /**
     * Simple No-op implementation of tracking.
     */
    class SimpleTracker implements Tracker {
        @DebugLog @Override public void track(String eventName) {}
        @DebugLog @Override public void track(String eventName, String... properties) {}
        @DebugLog @Override public void track(String eventName, Map<String, ?> properties) {}
    }

    /**
     * Replay.io implementation of tracking
     */
    class ReplayTracker implements Tracker {

        public ReplayTracker(Context context, Configuration appConfig) {
            // Set-Up Replay Settings
            String replayApiKey = appConfig.getReplayAPIKey();
            Config config = new Config(replayApiKey);
            config.setDebug(BuildConfig.DEBUG);
            config.setDispatchInterval(60000);
            config.setEnabled(true);
            config.setFlushAt(20);
            ReplayIO.init(context, config);
            track(AnalyticsEvents.Lifecycle.Start);
        }

        @Override
        public void track(String eventName) {
            ReplayIO.track(eventName);
        }

        @Override
        public void track(String eventName, String... properties) {
            ReplayIO.track(eventName, (Object[]) properties);
        }

        @Override
        public void track(String eventName, Map<String, ?> properties) {
            ReplayIO.track(eventName, properties);
        }
    }
}
