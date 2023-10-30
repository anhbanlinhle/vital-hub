package com.example.vital_hub.client.spring.objects;

import com.example.vital_hub.friend.Friend;
public class FriendListResponse {
    private final String message;
    private final Boolean success;
    private final Friend[] data;

    public FriendListResponse(String message, Boolean success, Friend[] data) {
        this.message = message;
        this.success = success;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public Boolean getSuccess() {
        return success;
    }

    public Friend[] getData() {
        return data;
    }
}
