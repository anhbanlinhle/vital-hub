package com.example.vital_hub.client.spring.objects;

public class CompetitionDurationResponse {
    private String message;
    private Boolean success;
    private String data;

    public CompetitionDurationResponse(String message, Boolean success, String duration) {
        this.message = message;
        this.success = success;
        this.data = duration;
    }

    public String getMessage() {
        return message;
    }
    public Boolean getSuccess() {
        return success;
    }
    public String getDuration() {
        return data;
    }

}
