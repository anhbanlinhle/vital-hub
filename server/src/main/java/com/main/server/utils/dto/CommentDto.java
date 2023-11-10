package com.main.server.utils.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;

public interface CommentDto {
    Long getId();
    Long getUserId();
    String getContent();
    String getAvatar();
    String getProfileName();
    LocalDateTime getCreatedAt();
    LocalDateTime getUpdatedAt();
    default Boolean getIsOwned(){
        return getIsOwnedInt() == 1;
    }

    @JsonIgnore
    Integer getIsOwnedInt();
}
