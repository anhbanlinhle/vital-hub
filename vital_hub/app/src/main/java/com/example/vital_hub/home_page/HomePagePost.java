package com.example.vital_hub.home_page;

import java.time.LocalDateTime;

public class HomePagePost {
    private Long id;
    private Long userId;
    private String title;
    private Long joggingId;
    private Boolean isDeleted = false;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int profileIcon;
    private int postImage;
    private String message;

    public HomePagePost(Long id, Long userId, String title, Long joggingId, Boolean isDeleted, LocalDateTime createdAt, LocalDateTime updatedAt, int profileIcon, int postImage, String message) {
        this.id = id;
        this.userId = userId;
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
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
