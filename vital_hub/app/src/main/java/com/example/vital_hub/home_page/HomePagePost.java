package com.example.vital_hub.home_page;

import java.time.LocalDateTime;

public class HomePagePost {
    private String avatar;
    private String image;
    private Long postId;
    private Long userId;
    private String username;
    private String title;
    private String createdAt;
    private Boolean isOwned;
    private Float calo;
    private String type;
    private String score;

    private Long exerciseId;

    public HomePagePost() {
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

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getOwned() {
        return isOwned;
    }

    public void setOwned(Boolean owned) {
        isOwned = owned;
    }

    public Float getCalo() {
        return calo;
    }

    public void setCalo(Float calo) {
        this.calo = calo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}
