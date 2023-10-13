package com.main.server.service;

import com.main.server.entity.WorkoutExercises;
import com.main.server.utils.dto.GroupExerciseDto;

import java.util.List;

public interface WorkoutExercisesService {
    List<GroupExerciseDto> getExerciseGroups();

    List<WorkoutExercises> getAllExercises();

    WorkoutExercises singleExerciseById(Long id);

    List<WorkoutExercises> getAllExercisesByGroupId(Long id);
}
