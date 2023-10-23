package com.main.server.utils.dto;

import java.time.LocalDateTime;

public interface PostDto {
    Long getUserId();
    String getUsername();
    Long getPostId();
    String getTitle();
    LocalDateTime getCreatedAt();
}
