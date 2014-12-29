package com.btellez.solidandroid.network;

import android.content.Context;

import com.google.gson.JsonObject;
import com.squareup.otto.Bus;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import hugo.weaving.DebugLog;

@Singleton
public class SimpleApi {
    private static final String TAG = SimpleApi.class.getSimpleName();

    Context context; // Injected via constructor
    Bus eventBus; // Injected via constructor

    @Inject public SimpleApi(Bus bus, Context ctx) {
        eventBus = bus;
        context = ctx;
    }

    public void getAllData() {
        eventBus.post(new Failure(new IOException()));
    }

    @DebugLog
    public boolean isValidResult(Exception e, JsonObject result) {
        // TODO: Implement a better is valid method.
        return e == null && result != null;
    }

    //region start-event-types
    public static class Success extends NetworkResult<JsonObject> {
        public Success(JsonObject result) {
            super(result);
        }
    }

    public static class Failure extends NetworkResult<Exception> {
        public Failure(Exception result) {
            super(result);
        }
    }

    public static abstract class NetworkResult<T> {
        private T result;
        public NetworkResult(T result) {
            this.result = result;
        }

        public T getResult() {
            return result;
        }
    }
    //end-region
}
