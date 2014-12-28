package com.btellez.solidandroid.network;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.squareup.otto.Bus;

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

    FutureCallback<JsonObject> standardCallback = new FutureCallback<JsonObject>() {
        @Override
        public void onCompleted(Exception e, JsonObject result) {
            if (!isValidResult(e, result)) {
                Log.d(TAG, e.getMessage());
                eventBus.post(new Failure(e));
                return;
            }
            eventBus.post(new Success(result));
            Log.d(TAG, result.toString());
        }
    };

    public void getAllData() {
        Ion.with(context)
                .load("http://redditapi.herokuapp.com/json")
                .asJsonObject()
                .setCallback(standardCallback);
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
