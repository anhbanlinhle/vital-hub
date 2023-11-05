package com.main.server.service;

import com.main.server.utils.dto.ExerciseDto;

import java.util.List;

public interface ExerciseService {

    List<ExerciseDto> getAllExerciseByUserId(Long userId, Integer page, Integer pageSize);
}
