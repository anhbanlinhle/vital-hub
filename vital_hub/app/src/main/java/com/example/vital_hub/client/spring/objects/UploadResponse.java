package com.example.vital_hub.client.spring.objects;

import com.google.gson.annotations.SerializedName;

public class UploadResponse {
    @SerializedName("data")
    private ImageData data;

    @SerializedName("success")
    private boolean success;

    @SerializedName("status")
    private int status;

    public ImageData getData() {
        return data;
    }

    public boolean isSuccess() {
        return success;
    }

    public int getStatus() {
        return status;
    }
}
