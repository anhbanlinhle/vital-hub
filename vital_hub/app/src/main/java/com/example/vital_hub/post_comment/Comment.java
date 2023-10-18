package com.example.vital_hub.post_comment;

import com.example.vital_hub.home_page.HomePagePost;

public class Comment {
    private int profileIcon;
    private String profileName;
    private String comment;

    private HomePagePost post;

    public Comment(int profileIcon, String profileName, String comment) {
        this.profileIcon = profileIcon;
        this.profileName = profileName;
        this.comment = comment;
    }

    public Comment(HomePagePost post) {
        this.post = post;
    }

    public HomePagePost getPost() {
        return post;
    }

    public void setPost(HomePagePost post) {
        this.post = post;
    }

    public int getProfileIcon() {
        return profileIcon;
    }

    public void setProfileIcon(int profileIcon) {
        this.profileIcon = profileIcon;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
