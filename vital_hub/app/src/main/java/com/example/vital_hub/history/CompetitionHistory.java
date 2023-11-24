package com.example.vital_hub.history;

public class CompetitionHistory {
    private String competitionName;
    private String background;
    private String endedAt;
    private Integer participants;


    public CompetitionHistory(String competitionName, String background, String profilePicture, String hostName, String endedAt, Integer participants) {
        this.competitionName = competitionName;
        this.background = background;
        this.endedAt = endedAt;
        this.participants = participants;
    }

    public String getCompetitionName() {
        return competitionName;
    }
    public String getBackground() {
        return background;
    }
    public String getEndedAt() {
        return endedAt;
    }
    public Integer getParticipants() {
        return participants;
    }
}
