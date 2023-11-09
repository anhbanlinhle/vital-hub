package com.example.vital_hub.home_page;

import java.time.LocalDateTime;

public class HomePagePost {
    private String avatar;
    private String image;
    private Long postId;
    private Long userId;
    private String username;
    private String title;
    private Long exerciseId;
    private Boolean isDeleted = false;
    private String createdAt;
    private String updatedAt;
    private Boolean isOwned;
    private Float calo;
    private String type;
    private String score;


    public HomePagePost(String image, String username, String title) {
        this.image = image;
        this.username = username;
        this.title = title;
    }

    public HomePagePost() {
    }

    public HomePagePost(String avatar) {
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Long getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(Long exerciseId) {
        this.exerciseId = exerciseId;
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
