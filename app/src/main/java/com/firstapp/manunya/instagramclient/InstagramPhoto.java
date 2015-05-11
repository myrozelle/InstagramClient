package com.firstapp.manunya.instagramclient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Manunya on 5/6/2015.
 */
public class InstagramPhoto {
    private String username;
    private String caption = "";
    private String imageURL;
    private String imageHeight;
    private int likesCount;
    private String profilePhotoURL;
    private String created_time;
    private int commentsCount;
    private ArrayList<Comment> comments;

    public void setData(JSONObject photoJSON) {
        //decode the attributes of the json into a data model
        try {
            this.username = photoJSON.getJSONObject("user").getString("username");
            if (photoJSON.optJSONObject("caption") != null) {
                this.caption = photoJSON.getJSONObject("caption").getString("text");
            }
            //photoJSON.getJSONObject("type");
            this.imageURL = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
            this.imageHeight = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getString("height");
            this.likesCount = photoJSON.getJSONObject("likes").getInt("count");
            this.profilePhotoURL = photoJSON.getJSONObject("user").getString("profile_picture");
            this.created_time = photoJSON.getString("created_time");
            //get comment count and comments
            this.commentsCount = photoJSON.getJSONObject("comments").getInt("count");
            if (this.commentsCount > 0) {
                this.comments = new ArrayList<>();
                JSONArray commentsJSON = photoJSON.getJSONObject("comments").getJSONArray("data");
                //add comments from oldest to newest
                for (int i = 0; i < commentsJSON.length(); i++) {
                    Comment comment = new Comment();
                    comment.setUsername(commentsJSON.getJSONObject(i).getJSONObject("from").getString("username"));
                    comment.setCreated_time(commentsJSON.getJSONObject(i).getString("created_time"));
                    comment.setCommentString(commentsJSON.getJSONObject(i).getString("text"));
                    comment.setProfilePhotoURL(commentsJSON.getJSONObject(i).getJSONObject("from").getString("profile_picture"));
                    this.comments.add(comment);
                }
                //Log.i("comments", this.comments.toString());
                //looks like comments are sorted so this part isn't needed
                /*Arrays.sort(this.comments, new Comparator<Comment>() {
                    @Override
                    public int compare(Comment c1, Comment c2) {
                        return c2.created_time.compareTo(c1.created_time);
                    }
                });*/
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public String getUsername() {
        return this.username;
    }

    public  String getCaption() {
        return  this.caption;
    }

    public String getImageURL() {
        return this.imageURL;
    }

    public String getProfilePhotoURL() {
        return this.profilePhotoURL;
    }

    public String getCreated_time() {
        return  this.created_time;
    }

    public int getLikesCount() {
        return this.likesCount;
    }

    public int getCommentsCount() {
        return this.commentsCount;
    }

    public ArrayList<Comment> getComments() {
        return this.comments;
    }

    public Comment getComment(int i) {
        return this.comments.get(i);
    }

    //class method to shorten the timespan string
    public static String shortenTimeSpanString (String tString) {
        if (tString.contains("hour")) {
            return tString.split(" ")[0] + "h";
        } else if (tString.contains("minute")) {
            return tString.split(" ")[0] + "m";
        } else if (tString.contains("second")) {
            return tString.split(" ")[0] + "s";
        }
        return tString;
    }
}
