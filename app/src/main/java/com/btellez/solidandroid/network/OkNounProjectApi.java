package com.btellez.solidandroid.network;

import android.os.Handler;
import android.os.Looper;

import com.btellez.solidandroid.configuration.Configuration;
import com.btellez.solidandroid.model.Icon;
import com.btellez.solidandroid.model.IconParser;
import com.google.gson.JsonParseException;
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
        Request request = buildRequestPublic(endpoint);
        client.newCall(request).enqueue(new OkHttpCallback(callback, "icons"));
    }

    @Override
    public void recent(final Callback callback) {
        String endpoint = new EndpointBuilder().buildRecentUrl(configuration);
        Request request = buildRequestOAuth(endpoint, NounProjectOAuth.RequestType.GET);
        client.newCall(request).enqueue(new OkHttpCallback(callback, "recent_uploads"));
    }


    /**
     * Builds Requests for public endpoints that do not require authentication.
     *
     * @param endpoint
     * @return Request
     */
    private Request buildRequestPublic(String endpoint) {
        return new Request.Builder().url(endpoint)
                                    .addHeader("X-Requested-With", "XMLHttpRequest")
                                    .build();
    }


    /**
     * Builds Request for endpoints that do require authentication.
     * @param endpoint
     * @param method
     * @return Request
     */
    private Request buildRequestOAuth(String endpoint, NounProjectOAuth.RequestType method) {
        String oAuthString = oAuth.withEnpoint(endpoint)
                                  .withRequestType(method)
                                  .getOAuthHeader();

        return new Request.Builder().url(endpoint)
                                    .addHeader("Authorization", oAuthString)
                                    .build();
    }

    /**
     * OkHttpCallback wrapper the app's defined callback interface.
     * This allows the the callback implementation to be decoupled form okHttp.
     */
    private class OkHttpCallback implements com.squareup.okhttp.Callback {
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
            try {
                String jsonString = response.body().string();
                final List<Icon> result = iconParser.fromJson(jsonString, dataKey);
                // We need to post update to the UI thread.
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onSuccess(result);
                    }
                });
            } catch (final JsonParseException nonJsonResponse) {
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onFailure(nonJsonResponse);
                    }
                });
            }
        }
    }
}
