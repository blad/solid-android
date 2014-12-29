package com.btellez.solidandroid.model;

import java.io.Serializable;

/**
 * Object representing icon information returned
 * by the NounProject API.
 */
public class Icon implements Serializable {
    private String attribution;
    private String attribution_icon_url;
    private String attribution_preview_url;
    private int count_download;
    private int count_purchase;
    private int count_view;
    private String date_uploaded;
    private String id;
    private String is_active;
    private String license_description;
    private String permalink;
    private String preview_url;
    private String preview_url_42;
    private String preview_url_84;
    private String sponsor_campaign_link;
    private String sponsor_id;
    private String term;
    private String term_id;
    private String term_slug;
    private Uploader uploader;
    private String uploader_id;
    private int year;

    public String getAttribution() {
        return attribution;
    }

    public int getDownloadCount() {
        return count_download;
    }

    public int getPurchaseCount() {
        return count_purchase;
    }

    public int getViewCount() {
        return count_view;
    }

    public String getPermalink() {
        return permalink;
    }

    public String getTerm() {
        return term;
    }

    public Uploader getUploader() {
        return uploader;
    }
}
