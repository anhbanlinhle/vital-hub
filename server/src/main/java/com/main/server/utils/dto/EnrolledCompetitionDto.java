package com.main.server.utils.dto;

public interface EnrolledCompetitionDto {
    Integer getPosition();
    Integer getCompetitionId();
    String getType();
    Integer getParticipants();
    String getBackground();
    String getEndedAt();
    String getTitle();

    default String getPositionStr() {
        if (getPosition() == null) {
            return null;
        }
        if (getPosition() == 1) {
            return getPosition() + "st";
        } else if (getPosition() == 2) {
            return getPosition() + "nd";
        } else if (getPosition() == 3) {
            return getPosition() + "rd";
        } else {
            return getPosition() + "th";
        }
    }
}
