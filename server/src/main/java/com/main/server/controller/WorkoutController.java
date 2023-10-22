package com.main.server.controller;

import com.main.server.entity.WorkoutExercises;
import com.main.server.middleware.TokenParser;
import com.main.server.service.WorkoutExercisesService;
import com.main.server.utils.dto.GroupExerciseDto;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/workout")
@AllArgsConstructor
public class WorkoutController {

    @Autowired
    WorkoutExercisesService workoutExercisesService;

    @Autowired
    private final TokenParser tokenParser;

    @GetMapping("/exercise-groups")
    public ResponseEntity<?> exerciseGroups(@RequestParam(name = "suggest") Boolean isSuggest,
                                            @RequestHeader("Authorization") String token) {
        List<GroupExerciseDto> groupExercise = workoutExercisesService.getExerciseGroups(isSuggest, tokenParser.getCurrentUserId(token));
        if (groupExercise == null || groupExercise.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.ok().body(groupExercise);
    }

    @GetMapping("/all-exercises")
    public ResponseEntity<?> allExercises(@RequestParam("page") Integer page,
                                          @RequestParam("pageSize") Integer pageSize,
                                          @RequestParam("order") String order,
                                          @RequestParam("desc") Boolean desc) {
        List<WorkoutExercises> groupExercise = workoutExercisesService.getAllExercises(page, pageSize, order, desc);
        if (groupExercise == null || groupExercise.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.ok().body(groupExercise);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> exerciseById(@PathVariable(name = "id") Long id) {
        WorkoutExercises result = workoutExercisesService.singleExerciseById(id);
        return result != null ? ResponseEntity.ok().body(result) : ResponseEntity.badRequest().body(null);
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
