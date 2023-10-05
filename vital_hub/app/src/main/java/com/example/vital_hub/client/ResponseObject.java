package com.example.vital_hub.client;

import com.google.gson.annotations.SerializedName;

public class ResponseObject {
    private String param1;
    private int param2;
    private boolean param3;
    @SerializedName("body")
    private String text;

    public String getParam1() {
        return param1;
    }

    public int getParam2() {
        return param2;
    }

    public boolean getParam3() {
        return param3;
    }

    public String getText() {
        return text;
    }

}
