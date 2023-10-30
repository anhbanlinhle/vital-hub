package com.example.vital_hub.client.spring.objects;

import android.content.Intent;

import com.google.gson.annotations.SerializedName;

public class ResponseObject {
    private String param1;
    private Integer param2;
    private Boolean param3;
    private String data;
    @SerializedName("body")
    private String text;
    public ResponseObject(String param1, Integer param2, Boolean param3) {
        this.param1 = param1;
        this.param2 = param2;
        this.param3 = param3;
    }

    public String getParam1() {
        return param1;
    }

    public int getParam2() {
        return param2;
    }

    public boolean getParam3() {
        return param3;
    }
    public String getData() {
        return data;
    }

    public String getText() {
        return text;
    }

}
