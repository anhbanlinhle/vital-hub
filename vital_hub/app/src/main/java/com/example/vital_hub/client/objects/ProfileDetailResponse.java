package com.example.vital_hub.client.objects;

import com.example.vital_hub.profile.UserDetail;

public class ProfileDetailResponse {
    private String message;
    private Boolean success;
    private UserDetail data;

    public ProfileDetailResponse(String message, Boolean success, UserDetail data) {
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

    public UserDetail getData() {
        return data;
    }

}
