package com.btellez.solidandroid.network;

import com.btellez.solidandroid.configuration.Configuration;
import com.btellez.solidandroid.model.Icon;

import java.util.List;

public interface NounProjectApi {
    public void search(String term, Callback callback);
    public void recent(Callback callback);

    public static class EndpointBuilder {
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
    public interface Callback {
        public void onSuccess(List<Icon> icons);
        public void onFailure(Throwable error);
    }

    public static class SimpleCallback implements Callback {
        @Override public void onSuccess(List<Icon> icons) {/* no-op */}
        @Override public void onFailure(Throwable error) {/* no-op */}
    }
}
