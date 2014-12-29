package com.btellez.solidandroid.configuration;

import android.content.Context;

import com.btellez.solidandroid.R;
import com.btellez.solidandroid.model.ApiKeys;

public interface Configuration {
    public ApiKeys getNounProjectApiKeys();
    public String getNounProjectBaseUrl();

    /**
     * Shared configuration settings for Release and Development.
     */
    public static abstract class BaseConfiguration implements Configuration {
        protected Context context;

        protected BaseConfiguration(Context context) {
            this.context = context;
        }

        @Override
        public String getNounProjectBaseUrl() {
            return context.getString(R.string.noun_project_base_url);
        }
    }

    /**
     * Configuration for the Release Version of our Application.
     */
    public static class ReleaseConfiguration extends BaseConfiguration {

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
    public static class DevelopmentConfiguration extends BaseConfiguration {

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
