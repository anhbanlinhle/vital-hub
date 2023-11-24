package com.example.vital_hub.client.spring.objects;

public class CompetitionHistoryListResponse {
    private String type;
    private Long position;
    private Long competitionId;
    private String background;
    private String endedAt;
    private Integer participants;
    private String positionStr;
    public CompetitionHistoryListResponse(String type, Long position, Long competitionId, String background, String endedAt, Integer participants, String positionStr) {
        this.type = type;
        this.position = position;
        this.competitionId = competitionId;
        this.background = background;
        this.endedAt = endedAt;
        this.participants = participants;
        this.positionStr = positionStr;
    }
    public String getType() {
        return type;
    }
    public Long getPosition() {
        return position;
    }
    public Long getCompetitionId() {
        return competitionId;
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
    public String getPositionStr() {
        return positionStr;
    }

}
