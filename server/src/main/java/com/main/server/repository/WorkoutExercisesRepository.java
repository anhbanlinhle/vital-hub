package com.main.server.repository;

import com.main.server.entity.WorkoutExercises;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkoutExercisesRepository extends JpaRepository<WorkoutExercises, Long> {

}
