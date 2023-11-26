package com.main.server.utils.dto;

import java.time.LocalDate;

public interface WeeklyExerciseDto {
    Float getCalo();
    Float getDistance();
    Integer getGymGroup();
    Integer getRep();
    Integer getStep();
    Integer getTotalTime(); // second

    LocalDate getDate();
}
