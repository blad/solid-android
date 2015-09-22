package com.btellez.solidandroid.configuration;

import android.content.Context;

import com.btellez.solidandroid.R;
import com.btellez.solidandroid.model.ApiKeys;

public interface Configuration {
    ApiKeys getNounProjectApiKeys();
    String getNounProjectBaseUrl();
    String getNounProjectBaseApiUrl();
    String getReplayAPIKey();

    /**
     * Shared configuration settings for Release and Development.
     */
    abstract class BaseConfiguration implements Configuration {
        protected Context context;

        protected BaseConfiguration(Context context) {
            this.context = context;
        }

        @Override
        public String getNounProjectBaseApiUrl() {
            return context.getString(R.string.noun_project_base_api_url);
        }

        @Override
        public String getNounProjectBaseUrl() {
            return context.getString(R.string.noun_project_base_url);
        }

        @Override
        public String getReplayAPIKey() {
            return context.getString(R.string.replay_io_api_key);
        }
    }

    /**
     * Configuration for the Release Version of our Application.
     */
    class ReleaseConfiguration extends BaseConfiguration {

        protected ReleaseConfiguration(Context context) {
            super(context);
        }

        @Override
        public ApiKeys getNounProjectApiKeys() {
            return new ApiKeys() {
                @Override public String getKey() {
                    return context.getResources().getStringArray(R.array.noun_project_api_key)[1];
                }

                @Override public String getSecret() {
                    return context.getResources().getStringArray(R.array.noun_project_api_secret)[1];
                }
            };
        }
    }


    /**
     * Configuration for the Development version of our application
     */
    class DevelopmentConfiguration extends BaseConfiguration {

        public DevelopmentConfiguration(Context context) {
            super(context);
        }

        @Override
        public ApiKeys getNounProjectApiKeys() {
            return new ApiKeys() {
                @Override public String getKey() {
                    return context.getResources().getStringArray(R.array.noun_project_api_key)[0];
                }

                @Override public String getSecret() {
                    return context.getResources().getStringArray(R.array.noun_project_api_secret)[0];
                }
            };
        }
    }

}
