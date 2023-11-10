package com.example.vital_hub.competition.data;

public class CompetitionEdit {

    public CompetitionEdit() {}

    public CompetitionEdit(Long id, String duration, String startedAt, String endedAt, String title, String background) {
        this.id = id;
        this.duration = duration;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
        this.title = title;
        this.background = background;
    }

    private Long id;
    private String duration;
    private String startedAt;
    private String endedAt;
    private String title;
    private String background;

    public void setBackground(String background) {
        this.background = background;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setEndedAt(String endedAt) {
        this.endedAt = endedAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setStartedAt(String startedAt) {
        this.startedAt = startedAt;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDuration() {
        return duration;
    }

    public String getBackground() {
        return background;
    }

    public String getTitle() {
        return title;
    }

    public Long getId() {
        return id;
    }

    public String getEndedAt() {
        return endedAt;
    }

    public String getStartedAt() {
        return startedAt;
    }
}
