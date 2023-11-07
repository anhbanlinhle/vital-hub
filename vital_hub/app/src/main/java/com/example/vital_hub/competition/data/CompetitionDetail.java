package com.example.vital_hub.competition.data;

public class CompetitionDetail {
    private Long id;
    private String host;
    private String time;
    private String duration;
    private String participants;
    private String title;
    private String type;
    private String background;

    public String getBackground() {
        return background;
    }

    public String getDuration() {
        return duration;
    }

    public String getHost() {
        return host;
    }

    public Long getId() {
        return id;
    }

    public String getParticipants() {
        return participants;
    }

    public String getTime() {
        return time;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }
}
