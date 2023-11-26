package com.main.server.controller;

import com.main.server.middleware.TokenParser;
import com.main.server.service.ExerciseService;
import com.main.server.utils.dto.SaveExerciseAndCompetitionDto;
import com.main.server.utils.enums.ExerciseType;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/exercise")
public class ExerciseController {

    @Autowired
    ExerciseService exerciseService;

    @Autowired
    final TokenParser tokenParser;

    @GetMapping("")
    public ResponseEntity<?> getAllExerciseByUserId(@RequestParam(name = "page") Integer page,
                                                    @RequestParam(name = "pageSize") Integer pageSize,
                                                    @RequestHeader(name = "Authorization") String token) {
        return ResponseEntity.ok().body(exerciseService.getAllExerciseByUserId(tokenParser.getCurrentUserId(token), page, pageSize));
    }

    @PostMapping("/save")
    public ResponseEntity<?> getAllExerciseByUserId(@RequestBody SaveExerciseAndCompetitionDto saveExerciseAndCompetitionDto,
                                                    @RequestHeader(name = "Authorization") String token) {
        exerciseService.saveExercise(saveExerciseAndCompetitionDto, tokenParser.getCurrentUserId(token));
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/weekly-result")
    public ResponseEntity<?> getWeeklyResult(@RequestHeader(name = "Authorization") String token) {
        return ResponseEntity.ok().body(exerciseService.getWeeklyResult(tokenParser.getCurrentUserId(token)));
    }
}
