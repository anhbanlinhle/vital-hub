package com.example.vital_hub.client.spring.objects;

import com.example.vital_hub.utils.ExerciseType;

import java.time.LocalDateTime;

public class OriginExercise {
    private Long id;

    private LocalDateTime startedAt;

    private LocalDateTime endedAt;

    private Float calo;

    private Long userId;

    private ExerciseType type;

    public Long getId() {
        return id;
    }

    public ExerciseType getType() {
        return type;
    }

    public Float getCalo() {
        return calo;
    }

    public LocalDateTime getEndedAt() {
        return endedAt;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public Long getUserId() {
        return userId;
    }
}
