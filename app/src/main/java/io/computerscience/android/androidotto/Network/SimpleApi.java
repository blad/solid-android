package io.computerscience.android.androidotto.Network;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import javax.inject.Singleton;

@Singleton
public class SimpleApi {

    public void getAllData(Context context, final NetworkCallback callback) {
        // TODO: Generalize the network Call.
        Ion.with(context)
                .load("http://www.reddit.com/hot.json")
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (!isValidResult(e, result)) {
                            Log.e("LOG", e.getMessage());
                            callback.onFailure(e);
                            return;
                        }
                        Log.e("LOG", result.toString());
                        callback.onSuccess(result);
                    }
                });
    }

    public boolean isValidResult(Exception e, JsonObject result) {
        // TODO: Implement a better is valid method.
        return e == null && result != null;
    }

    public static interface NetworkCallback {
        public void onSuccess(JsonObject value);
        public void onFailure(Exception exception);
    }
}
