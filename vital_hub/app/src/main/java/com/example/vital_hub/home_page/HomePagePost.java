package com.example.vital_hub.home_page;

import java.time.LocalDateTime;

public class HomePagePost {
    private String avatar;
    private Long postId;
    private Long userId;
    private String username;
    private String title;
    private Long joggingId;
    private Boolean isDeleted = false;
    private String createdAt;
    private String updatedAt;
    private int profileIcon;
    private int postImage;
    private String message;

    public HomePagePost(String avatar, Long postId, Long userId, String username, String title, Long joggingId, Boolean isDeleted, String createdAt, String updatedAt, int profileIcon, int postImage, String message) {
        this.avatar = avatar;
        this.postId = postId;
        this.userId = userId;
        this.username = username;
        this.title = title;
        this.joggingId = joggingId;
        this.isDeleted = isDeleted;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.profileIcon = profileIcon;
        this.postImage = postImage;
        this.message = message;
    }

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

    public Long getId() {
        return postId;
    }

    public void setId(Long id) {
        this.postId = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getJoggingId() {
        return joggingId;
    }

    public void setJoggingId(Long joggingId) {
        this.joggingId = joggingId;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
