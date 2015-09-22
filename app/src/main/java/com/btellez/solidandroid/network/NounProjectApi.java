package com.btellez.solidandroid.network;

import com.btellez.solidandroid.configuration.Configuration;
import com.btellez.solidandroid.model.Icon;

import java.util.List;

public interface NounProjectApi {
    void search(String term, Callback callback);
    void recent(Callback callback);

    /**
     * Endpoint Helper that builds correctly formatter URLs
     * for the NounProjectAPI.
     */
    class EndpointBuilder {
        public String buildSearchUrl(Configuration config, String term) {
            return config.getNounProjectBaseUrl() + "/search/json/icon/?q=" + term + "/?page=1&limit=100&offset=0&raw_html=false";
        }

        public String buildRecentUrl(Configuration config) {
            return config.getNounProjectBaseApiUrl() + "icons/recent_uploads";
        }
    }

    /**
     * Callback that alerts any listeners about success or failure of the api call.
     */
    interface Callback {
        void onSuccess(List<Icon> icons);
        void onFailure(Throwable error);
    }


    /**
     * No-Op Implementation of the Callback that allows overriding select methods,
     * instead of forcing implementor to override all methods.
     */
    class SimpleCallback implements Callback {
        @Override public void onSuccess(List<Icon> icons) {/* no-op */}
        @Override public void onFailure(Throwable error) {/* no-op */}
    }
}
