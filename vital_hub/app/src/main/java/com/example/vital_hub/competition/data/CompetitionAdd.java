package com.example.vital_hub.competition.data;

public class CompetitionAdd {
    private String duration;
    private String startedAt;
    private String endedAt;
    private String title;
    private String background;
    private String type;

    public CompetitionAdd() {}
    public CompetitionAdd(String duration, String startedAt, String endedAt, String title, String background, String type) {
        this.duration = duration;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
        this.title = title;
        this.background = background;
        this.type = type;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(String startedAt) {
        this.startedAt = startedAt;
    }

    public String getEndedAt() {
        return endedAt;
    }

    public void setEndedAt(String endedAt) {
        this.endedAt = endedAt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
