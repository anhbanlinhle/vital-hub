package com.example.vital_hub.home_page;

public class HomePagePost {

    private int profileIcon;
    private int postImage;
    private String title;
    private String message;

    public HomePagePost(int profileIcon, int postImage, String title, String message) {
        this.profileIcon = profileIcon;
        this.postImage = postImage;
        this.title = title;
        this.message = message;
    }

    public int getProfileIcon() {
        return profileIcon;
    }

    public void setProfileIcon(int profileIcon) {
        this.profileIcon = profileIcon;
    }

    public int getPostImage() {
        return postImage;
    }

    public void setPostImage(int postImage) {
        this.postImage = postImage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
