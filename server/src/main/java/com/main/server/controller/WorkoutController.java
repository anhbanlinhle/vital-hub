package com.main.server.controller;

import com.main.server.entity.WorkoutExercises;
import com.main.server.service.WorkoutExercisesService;
import com.main.server.utils.dto.GroupExerciseDto;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/workout")
@AllArgsConstructor
public class WorkoutController {

    @Autowired
    WorkoutExercisesService workoutExercisesService;

    @GetMapping("/exercise-groups")
    public ResponseEntity<?> exerciseGroups() {
        List<GroupExerciseDto> groupExercise = workoutExercisesService.getExerciseGroups();
        if (groupExercise == null || groupExercise.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.ok().body(groupExercise);
    }

    @GetMapping("/all-exercises")
    public ResponseEntity<?> allExercises() {
        List<WorkoutExercises> groupExercise = workoutExercisesService.getAllExercises();
        if (groupExercise == null || groupExercise.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.ok().body(groupExercise);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> exerciseById(@PathVariable(name = "id") Long id) {
        WorkoutExercises result = workoutExercisesService.singleExerciseById(id);
        return result == null ? ResponseEntity.ok().body(result) : ResponseEntity.badRequest().body(null);
    }

    @GetMapping("/group/{id}")
    public ResponseEntity<?> groupExerciseById(@PathVariable(name = "id") Long id) {
        List<WorkoutExercises> groupExercise = workoutExercisesService.getAllExercisesByGroupId(id);
        if (groupExercise == null || groupExercise.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.ok().body(groupExercise);
    }
}
