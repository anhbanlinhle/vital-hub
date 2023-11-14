package com.example.vital_hub.client.spring.objects;

public class SaveExerciseAndCompetitionDto {
    private Long competitionId;
    private OriginExercise exercise;
    private Integer step;
    private Integer rep;
    private Long groupId;
    private Float distance;

    public Float getDistance() {
        return distance;
    }

    public Integer getRep() {
        return rep;
    }

    public Long getCompetitionId() {
        return competitionId;
    }

    public OriginExercise getExercise() {
        return exercise;
    }

    public Integer getStep() {
        return step;
    }

    public Long getGroupId() {
        return groupId;
    }
}
