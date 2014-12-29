package com.btellez.solidandroid.model;

import java.io.Serializable;

/**
 * Object Representing user information returned
 * by the NounProject API.
 */
public class Uploader implements Serializable{
    private String location;
    private String name;
    private String permalink;
    private String username;

    public String getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    public String getPermalink() {
        return permalink;
    }

    public String getUsername() {
        return username;
    }
}
