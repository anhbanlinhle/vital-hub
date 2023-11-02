package com.example.vital_hub.client.spring.objects;

import com.google.gson.annotations.SerializedName;

public class ImageData {
    @SerializedName("url")
    private String url;

    public String getUrl() {
        return url;
    }
}
