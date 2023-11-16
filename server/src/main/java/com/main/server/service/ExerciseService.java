package com.main.server.service;

import com.main.server.utils.dto.ExerciseDto;
import com.main.server.utils.dto.SaveExerciseAndCompetitionDto;
import com.main.server.utils.enums.ExerciseType;

import java.util.List;

public interface ExerciseService {

    List<ExerciseDto> getAllExerciseByUserId(Long userId, Integer page, Integer pageSize);

    SaveExerciseAndCompetitionDto saveExercise(SaveExerciseAndCompetitionDto saveExerciseAndCompetitionDto,
                      Long userId);

    Object getWeeklyResult(ExerciseType exerciseType, Long userId);
}
