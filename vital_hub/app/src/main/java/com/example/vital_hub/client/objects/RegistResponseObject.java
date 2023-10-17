package com.example.vital_hub.client.objects;

import com.google.gson.annotations.SerializedName;
public class RegistResponseObject {

    @SerializedName("body")
    private String text;

    public String getText() {
        return text;
    }
}
