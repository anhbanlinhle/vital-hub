package com.example.vital_hub.competition;

import java.sql.Date;

public class Competition {
    private final Long id;
    private final String title;
    private final String background;
    private final Long remainDay;
    private final Integer isOngoing;
    private final Integer type;
    private final Long hostId;
    private final String hostName;
    private final String hostAvatar;
    private final Integer participantCount;

    public Competition(Long id, String title, String background, Long remainDay, Integer isOngoing, Integer type, Long hostId, String hostName, String hostAvatar, Integer participantCount, Integer status) {
        this.id = id;
        this.title = title;
        this.background = background;
        this.remainDay = remainDay;
        this.isOngoing = isOngoing;
        this.type = type;
        this.hostId = hostId;
        this.hostName = hostName;
        this.hostAvatar = hostAvatar;
        this.participantCount = participantCount;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getBackground() {
        return background;
    }

    public Long getRemainDay() {
        return remainDay;
    }

    public Boolean getIsOngoing() {
        return isOngoing == 1;
    }

    public String getType() {
        switch (type){
            case 2:
                return "Bicycling";
            case 3:
                return "PushUp";
            default:
                return "Running";
        }
    }

    public Long getHostId() {
        return hostId;
    }

    public String getHostName() {
        return hostName;
    }

    public String getHostAvatar() {
        return hostAvatar;
    }

    public Integer getParticipantCount() {
        return participantCount;
    }

}
