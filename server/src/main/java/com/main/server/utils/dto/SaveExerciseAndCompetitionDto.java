package com.main.server.utils.dto;

import com.main.server.entity.Exercise;
import com.main.server.utils.enums.ExerciseType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaveExerciseAndCompetitionDto implements Serializable {

    private static final long serialVersionUID = -197553281792804396L;
    private Long competitionId;
    private Exercise exercise;
    private String startedAt;
    private Float calo;
    private ExerciseType type;
    private Integer step;
    private Integer rep;
    private Long groupId;
    private Float distance;
}
