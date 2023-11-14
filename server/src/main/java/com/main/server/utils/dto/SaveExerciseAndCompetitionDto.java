package com.main.server.utils.dto;

import com.main.server.entity.Exercise;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaveExerciseAndCompetitionDto {
    private Long competitionId;
    private Exercise exercise;
    private Integer step;
    private Integer rep;
    private Long groupId;
    private Float distance;
}
