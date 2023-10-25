package com.main.server.utils.dto;

import java.sql.Date;

public interface CompetitionListDto {
    Long getId();
    String getTitle();

    String getBackground();

    Long getRemainDay();

    Long getHostId();

    String getHostName();

    String getHostAvatar();

    Integer getParticipantCount();

    Integer getIsJoined();
}
