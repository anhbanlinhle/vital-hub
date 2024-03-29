package com.main.server.repository;

import com.main.server.entity.WorkoutExercises;
import com.main.server.utils.dto.GroupExerciseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface WorkoutExercisesRepository extends JpaRepository<WorkoutExercises, Long> {

    @Query("SELECT w.groupId AS groupId, COUNT(w.groupId) AS exerciseCount, SUM(w.totalCalo) AS totalCalo FROM WorkoutExercises w GROUP BY w.groupId")
    List<GroupExerciseDto> getGroupExercise();

    @Query("SELECT w.groupId AS groupId, COUNT(w.groupId) AS exerciseCount, SUM(w.totalCalo) AS totalCalo FROM WorkoutExercises w WHERE w.groupId = :groupId GROUP BY w.groupId")
    List<GroupExerciseDto> getGroupExerciseByGroupId(Long groupId);

    @Query("SELECT w FROM WorkoutExercises w")
    Page<WorkoutExercises> allExerciseOrderBy(Pageable pageable);

    Optional<WorkoutExercises> findById(Long id);

    @Query("SELECT w FROM WorkoutExercises w WHERE w.groupId = :id")
    List<WorkoutExercises> allExerciseByGroupId(Long id);
}