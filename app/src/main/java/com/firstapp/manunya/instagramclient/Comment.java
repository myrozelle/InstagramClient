package com.firstapp.manunya.instagramclient;

import java.io.Serializable;

/**
 * Created by Manunya on 5/10/2015.
 */
public class Comment implements Serializable{
    private String username;
    private String created_time;
    private String commentString;
    private String profilePhotoURL;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setCreated_time(String created_time) {
        this.created_time = created_time;
    }

    public void setCommentString(String commentString) {
        this.commentString = commentString;
    }

    public void setProfilePhotoURL(String profilePhotoURL) {
        this.profilePhotoURL = profilePhotoURL;
    }

    public String getUsername() {
        return username;
    }

    public String getCreated_time() {
        return created_time;
    }

    public String getCommentString() {
        return commentString;
    }

    public String getProfilePhotoURL() {
        return profilePhotoURL;
    }
}
