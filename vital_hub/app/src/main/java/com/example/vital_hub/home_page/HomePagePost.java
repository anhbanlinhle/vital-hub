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

    private Boolean isOwned;


    public HomePagePost(String avatar, String username, String title) {
        this.avatar = avatar;
        this.username = username;
        this.title = title;
    }

    public String getAvatar() {
        return avatar;
    }

    public Boolean getOwned() {
        return isOwned;
    }

    public void setOwned(Boolean owned) {
        isOwned = owned;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
