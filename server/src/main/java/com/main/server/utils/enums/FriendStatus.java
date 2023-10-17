package com.main.server.utils.enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum FriendStatus {
    ACCEPTED("ACCEPTED"),
    PENDING("PENDING");

    private final String status;

}
