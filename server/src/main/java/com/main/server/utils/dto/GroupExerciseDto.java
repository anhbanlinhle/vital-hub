package com.main.server.utils.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public interface GroupExerciseDto {
    Long getGroupId();
    Integer getExerciseCount();
    Float getTotalCalo();
}
