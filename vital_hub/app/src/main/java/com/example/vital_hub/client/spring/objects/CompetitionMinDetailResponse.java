package com.example.vital_hub.client.spring.objects;

import com.example.vital_hub.competition.data.CompetitionMinDetail;

import java.util.List;

public class CompetitionMinDetailResponse {
    private final String message;
    private final Boolean success;
    private final List<CompetitionMinDetail> data;

    public CompetitionMinDetailResponse(String message, Boolean success, List<CompetitionMinDetail> data) {
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

    public List<CompetitionMinDetail>getData() {
        return data;
    }
}
