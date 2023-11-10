package com.example.vital_hub.post_comment;

import com.example.vital_hub.home_page.HomePagePost;

public class Comment {
    private String avatar;
    private String profileName;
    private Long id;
    private Long userId;
    private Long postId;
    private String content;
    private String createdAt;
    private String updatedAt;
    private HomePagePost post;
    private Boolean isOwned;

    public Comment(HomePagePost post) {
        this.post = post;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
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

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public HomePagePost getPost() {
        return post;
    }

    public void setPost(HomePagePost post) {
        this.post = post;
    }

    public Boolean getOwned() {
        return isOwned;
    }

    public void setOwned(Boolean owned) {
        isOwned = owned;
    }
}
