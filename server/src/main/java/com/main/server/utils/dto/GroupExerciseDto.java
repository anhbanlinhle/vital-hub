package com.main.server.utils.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupExerciseDto {
    private Long groupId;
    private Integer exerciseCount;
    private Float totalCalo;
}
