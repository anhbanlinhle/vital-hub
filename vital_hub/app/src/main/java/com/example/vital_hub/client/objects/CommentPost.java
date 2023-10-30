package com.example.vital_hub.client.objects;

import java.time.LocalDateTime;

public class CommentPost {
    private Long id;
    private Long userId;
    private Long postId;
    private String content;
    private Boolean isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CommentPost(Long postId, String content) {
        this.postId = postId;
        this.content = content;
        this.id = null;
        this.userId = null;
        this.isDeleted = null;
        this.createdAt = null;
        this.updatedAt = null;
    }
}
