package com.example.vital_hub.client.objects;

import com.example.vital_hub.profile.UserInfo;

public class ProfileResponse {
    private String message;
    private Boolean success;
    private UserInfo data;

    public ProfileResponse(String message, Boolean success, UserInfo data) {
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

    public UserInfo getData() {
        return data;
    }
}
