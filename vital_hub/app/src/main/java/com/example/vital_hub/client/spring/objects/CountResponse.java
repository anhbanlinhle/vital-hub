package com.example.vital_hub.client.spring.objects;

public class CountResponse {
    private String message;
    private Boolean success;
    private Integer data;

    public CountResponse(String message, Boolean success, Integer data) {
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

    public Integer getData() {
        return data;
    }
}
