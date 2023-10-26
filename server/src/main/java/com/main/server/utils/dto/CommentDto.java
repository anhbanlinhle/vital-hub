package com.main.server.utils.dto;

import java.time.LocalDateTime;

public interface CommentDto {
    Long getId();
    Long getUserId();
    String getContent();
    String getAvatar();
    String getProfileName();
    LocalDateTime getCreatedAt();
    LocalDateTime getUpdatedAt();
    Boolean getIsOwned();
}
