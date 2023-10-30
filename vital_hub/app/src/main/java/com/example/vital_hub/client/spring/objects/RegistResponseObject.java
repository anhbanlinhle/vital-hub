package com.example.vital_hub.client.spring.objects;

import com.google.gson.annotations.SerializedName;
public class RegistResponseObject {

    @SerializedName("body")
    private String text;

    public String getText() {
        return text;
    }
}
