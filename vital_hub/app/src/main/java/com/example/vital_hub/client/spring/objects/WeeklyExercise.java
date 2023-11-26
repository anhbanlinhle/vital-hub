package com.example.vital_hub.client.spring.objects;

public class WeeklyExercise {
    private Float calo;
    private Float distance;
    private Integer gymGroup;
    private Integer rep;
    private Integer step;
    private  Integer totalTime;
    private String date;

    public Integer getGymGroup() {
        return gymGroup;
    }

    public void setGymGroup(Integer gymGroup) {
        this.gymGroup = gymGroup;
    }
    public Float getCalo() {
        return calo;
    }

    public void setCalo(Float calo) {
        this.calo = calo;
    }

    public Float getDistance() {
        return distance;
    }

    public void setDistance(Float distance) {
        this.distance = distance;
    }
    public Integer getRep() {
        return rep;
    }

    public void setRep(Integer rep) {
        this.rep = rep;
    }

    public Integer getStep() {
        return step;
    }

    public void setStep(Integer step) {
        this.step = step;
    }

    public Integer getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(Integer totalTime) {
        this.totalTime = totalTime;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
