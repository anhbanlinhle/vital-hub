package com.example.vital_hub.client.spring.objects;

public class ExerciseResponse {
    private Long exerciseId;
    private String time;
    private Float calo;
    private String type;
    private String score;

    public Long getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(Long exerciseId) {
        this.exerciseId = exerciseId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Float getCalo() {
        return calo;
    }

    public void setCalo(Float calo) {
        this.calo = calo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}
