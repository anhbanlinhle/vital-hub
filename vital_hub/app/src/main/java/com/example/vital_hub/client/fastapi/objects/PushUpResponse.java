package com.example.vital_hub.client.fastapi.objects;

import com.google.gson.annotations.SerializedName;

public class PushUpResponse {
    @SerializedName("count")
    private int count;

    public int getCount() {
        return count;
    }
}
