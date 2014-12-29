package com.btellez.solidandroid.network;

import android.os.Handler;
import android.os.Looper;

import com.btellez.solidandroid.configuration.Configuration;
import com.btellez.solidandroid.model.Icon;
import com.btellez.solidandroid.model.IconParser;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

/**
 * Implementation of the NounProjectApi interface that uses
 * the OkHttp Library.
 */
public class OkNounProjectApi implements  NounProjectApi {
    @Inject Configuration configuration;
    @Inject IconParser iconParser;

    // Implementation Related Dependencies
    private NounProjectOAuth oAuth;
    private OkHttpClient client;

    public OkNounProjectApi(Configuration configuration, IconParser iconParser) {
        this.configuration = configuration;
        this.iconParser = iconParser;

        // Fulfil Implementation Related Dependencies
        this.client = new OkHttpClient();
        this.oAuth = new NounProjectOAuth(configuration.getNounProjectApiKeys());
    }

    @Override
    public void search(String term, final Callback callback) {
        String endpoint = new EndpointBuilder().buildSearchUrl(configuration, term);
        Request request = buildRequest(endpoint, NounProjectOAuth.RequestType.GET);
        client.newCall(request).enqueue(new OkHttpCallback(callback));
    }

    @Override
    public void recent(final Callback callback) {
        String endpoint = new EndpointBuilder().buildRecentUrl(configuration);
        Request request = buildRequest(endpoint, NounProjectOAuth.RequestType.GET);
        client.newCall(request).enqueue(new OkHttpCallback(callback, "recent_uploads"));
    }

    private Request buildRequest(String endpoint, NounProjectOAuth.RequestType method) {
        String oAuthString = oAuth.withEnpoint(endpoint)
                                    .withRequestType(method)
                                    .getOAuthHeader();

        return new Request.Builder().url(endpoint)
                                    .addHeader("Authorization", oAuthString)
                                    .build();
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
        private Handler uiHandler = new Handler(Looper.getMainLooper());

        private OkHttpCallback(Callback callback) {
            this(callback, null);
        }

        private OkHttpCallback(Callback callback, String dataKey) {
            this.callback = callback;
            this.dataKey = dataKey;
        }

        @Override public void onFailure(final Request request,final IOException e) {
            // We need to post update to the UI thread.
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    callback.onFailure(e);
                }
            });
        }

        @Override public void onResponse(final Response response) throws IOException {
            String jsonString = response.body().string();
            final List<Icon> result = iconParser.fromJson(jsonString, dataKey);
            // We need to post update to the UI thread.
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                        callback.onSuccess(result);
                }
            });

        }
    }
}
