package com.example.vital_hub.profile;

public class DetailedProfile {
    private Long id;
    private Long userId;
    private Double currentHeight;
    private Double currentWeight;
    private Integer exerciseDaysPerWeek;
    private String description;

    public DetailedProfile(Long id, Long userId, Double currentHeight, Double currentWeight, Integer exerciseDaysPerWeek, String description) {
        this.id = id;
        this.userId = userId;
        this.currentHeight = currentHeight;
        this.currentWeight = currentWeight;
        this.exerciseDaysPerWeek = exerciseDaysPerWeek;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public Double getCurrentHeight() {
        return currentHeight;
    }

    public Double getCurrentWeight() {
        return currentWeight;
    }

    public Integer getExerciseDaysPerWeek() {
        return exerciseDaysPerWeek;
    }

    public String getDescription() {
        return description;
    }
}
