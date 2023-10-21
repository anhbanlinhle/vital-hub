package com.main.server.service;

import com.main.server.entity.WorkoutExercises;
import com.main.server.utils.dto.GroupExerciseDto;

import java.util.List;

public interface WorkoutExercisesService {
    List<GroupExerciseDto> getExerciseGroups(Boolean isSuggest, Long userId);

    List<WorkoutExercises> getAllExercises(Integer page, Integer pageSize, String order);

    WorkoutExercises singleExerciseById(Long id);

    List<WorkoutExercises> getAllExercisesByGroupId(Long id);
}
