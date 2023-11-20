package com.example.vital_hub.client.spring.objects;

import com.example.vital_hub.utils.ExerciseType;

import java.time.LocalDateTime;

public class SaveExerciseAndCompetitionDto {
    private Long competitionId;
    private Integer step;
    private Integer rep;
    private Long groupId;
    private Float distance;
    private ExerciseType type;
    private String startedAt;
    private Float calo;

    public Float getDistance() {
        return distance;
    }

    public Integer getRep() {
        return rep;
    }

    public Long getCompetitionId() {
        return competitionId;
    }


    public Integer getStep() {
        return step;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setCompetitionId(Long competitionId) {
        this.competitionId = competitionId;
    }

    public void setStep(Integer step) {
        this.step = step;
    }

    public void setRep(Integer rep) {
        this.rep = rep;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public void setDistance(Float distance) {
        this.distance = distance;
    }

    public ExerciseType getType() {
        return type;
    }

    public String getStartedAt() {
        return startedAt;
    }

    public Float getCalo() {
        return calo;
    }

    public void setType(ExerciseType type) {
        this.type = type;
    }

    public void setStartedAt(String startedAt) {
        this.startedAt = startedAt;
    }

    public void setCalo(Float calo) {
        this.calo = calo;
    }
}
