package com.btellez.solidandroid.network;

import com.btellez.solidandroid.configuration.Configuration;
import com.btellez.solidandroid.model.IconParser;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import javax.inject.Inject;

/**
 * Implementation of the NounProjectApi interface that uses
 * the OkHttp Library.
 */
public class OkNounProjectApi implements  NounProjectApi {
    @Inject Configuration configuration;
    @Inject IconParser iconParser;
    private OkHttpClient client = new OkHttpClient();

    @Override
    public void search(String term, final Callback callback) {
        String endpoint = new EndpointBuilder().buildSearchUrl(configuration, term);
        Request request = new Request.Builder().url(endpoint).build();
        client.newCall(request).enqueue(new OkHttpCallback(callback, "icons"));
    }

    @Override
    public void recent(final Callback callback) {
        String endpoint = new EndpointBuilder().buildRecentUrl(configuration);
        Request request = new Request.Builder().url(endpoint).build();
        client.newCall(request).enqueue(new OkHttpCallback(callback, "recent_uploads"));
    }

    /**
     * OkHttpCallback implementation that calls back to out the callback
     * defined by out interface. This way our callback implementation
     * is not tied to okHttp, but to out own Callback, this decouples
     * the our app from OkHttp.
     */
    private class OkHttpCallback implements  com.squareup.okhttp.Callback {
        public Callback callback;
        public String dataKey;

        private OkHttpCallback(Callback callback, String dataKey) {
            this.callback = callback;
            this.dataKey = dataKey;
        }

        @Override public void onFailure(Request request, IOException e) {
            callback.onFailure(e);
        }

        @Override public void onResponse(Response response) throws IOException {
            callback.onSuccess(iconParser.fromJson(response.body().string(), dataKey));
        }
    }
}
