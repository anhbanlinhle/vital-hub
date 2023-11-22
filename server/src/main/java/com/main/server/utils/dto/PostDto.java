package com.main.server.utils.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;

public interface PostDto {
    Long getUserId();
    String getAvatar();
    String getUsername();
    Long getPostId();
    String getTitle();
    LocalDateTime getCreatedAt();

    String getImage();
    String getType();
    String getScore();
    Float getCalo();

    @JsonIgnore
    Integer getIsOwnedInt();

    default Boolean getIsOwned(){
        return getIsOwnedInt() == 1;
    }
}
