package com.example.vital_hub.client.objects;

import com.example.vital_hub.competition.Competition;

public class CompetitionListResponse {
    private final String message;
    private final Boolean success;
    private final Competition[] data;

    public CompetitionListResponse(String message, Boolean success, Competition[] data) {
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

    public Competition[] getData() {
        return data;
    }
}
