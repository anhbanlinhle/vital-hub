package com.example.vital_hub.post_comment;

import com.example.vital_hub.home_page.HomePagePost;

public class Comment {
    private String avatar;
    private Long id;
    private Long userId;
    private Long postId;
    private String content;
    private Boolean isDeleted;
    private String createdAt;
    private String  updatedAt;
    private int profileIcon;
    private String profileName;
    private HomePagePost post;

    public Comment(int profileIcon, String profileName, String content) {
        this.profileIcon = profileIcon;
        this.profileName = profileName;
        this.content = content;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
