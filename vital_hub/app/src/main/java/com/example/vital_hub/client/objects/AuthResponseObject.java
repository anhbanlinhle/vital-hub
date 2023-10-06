package com.example.vital_hub.client.objects;

import com.google.gson.annotations.SerializedName;

public class AuthResponseObject {
    private String msg;
    private String status;
    private String token;
    private Boolean isFirstSign;
    @SerializedName("body")
    private String text;

    public String getMsg() {
        return msg;
    }

    public String getStatus() {
        return status;
    }

    public String getToken() {
        return token;
    }

    public Boolean getFirstSign() {
        return isFirstSign;
    }

    public String getText() {
        return text;
    }
}
