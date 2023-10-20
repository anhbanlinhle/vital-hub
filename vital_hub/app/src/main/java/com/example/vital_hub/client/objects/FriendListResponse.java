package com.example.vital_hub.client.objects;

import com.example.vital_hub.model.Friend;
public class FriendListResponse {
    private String message;
    private Boolean success;
    private Friend[] data;

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
