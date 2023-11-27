package com.main.server.service;

import com.main.server.utils.dto.ExerciseDto;
import com.main.server.utils.dto.SaveExerciseAndCompetitionDto;
import com.main.server.utils.dto.WeeklyExerciseDto;
import com.main.server.utils.enums.ExerciseType;

import java.util.List;
import java.util.Map;

public interface ExerciseService {

    List<ExerciseDto> getAllExerciseByUserId(Long userId, Integer page, Integer pageSize);

    SaveExerciseAndCompetitionDto saveExercise(SaveExerciseAndCompetitionDto saveExerciseAndCompetitionDto,
                      Long userId);

    Map<ExerciseType, List<WeeklyExerciseDto>> getWeeklyResult(Long userId);
}
