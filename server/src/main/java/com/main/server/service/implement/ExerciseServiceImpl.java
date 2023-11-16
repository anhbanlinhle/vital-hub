package com.main.server.service.implement;

import com.main.server.entity.*;
import com.main.server.repository.*;
import com.main.server.service.ExerciseService;
import com.main.server.utils.dto.ExerciseDto;
import com.main.server.utils.dto.SaveExerciseAndCompetitionDto;
import com.main.server.utils.dto.WeeklyExerciseDto;
import com.main.server.utils.enums.ExerciseType;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.stream.IntStream;

@Service
@AllArgsConstructor
public class ExerciseServiceImpl implements ExerciseService {

    @Autowired
    ExerciseRepository exerciseRepository;

    @Autowired
    RunningRepository runningRepository;

    @Autowired
    BicyclingRepository bicyclingRepository;

    @Autowired
    GymRepository gymRepository;

    @Autowired
    PushUpRepository pushUpRepository;

    @Override
    public List<ExerciseDto> getAllExerciseByUserId(Long userId, Integer page, Integer pageSize) {
        return exerciseRepository.getAllExerciseWithUid(userId, pageSize, page*pageSize);
    }

    @Override
    public SaveExerciseAndCompetitionDto saveExercise(SaveExerciseAndCompetitionDto saveExerciseAndCompetitionDto,
                             Long userId) {
        Exercise exercise = Exercise.builder()
                .userId(userId)
                .startedAt(saveExerciseAndCompetitionDto.getExercise().getStartedAt())
                .calo(saveExerciseAndCompetitionDto.getExercise().getCalo())
                .type(saveExerciseAndCompetitionDto.getExercise().getType())
                .endedAt(LocalDateTime.now())
                .build();

        Exercise savedExercise = exerciseRepository.save(exercise);
        saveExerciseAndCompetitionDto.setExercise(savedExercise);

        if (savedExercise.getType() == ExerciseType.BICYCLING) {
            Bicycling bicycling = Bicycling.builder()
                                            .distance(saveExerciseAndCompetitionDto.getDistance())
                                            .exerciseId(savedExercise.getId())
                                            .build();

            bicyclingRepository.save(bicycling);
        } else if (savedExercise.getType() == ExerciseType.RUNNING) {
            Running running = Running.builder()
                    .step(saveExerciseAndCompetitionDto.getStep())
                    .exerciseId(savedExercise.getId())
                    .build();
            runningRepository.save(running);
        } else if (savedExercise.getType() == ExerciseType.PUSHUP) {
            PushUp pushUp = PushUp.builder()
                    .rep(saveExerciseAndCompetitionDto.getRep())
                    .exerciseId(savedExercise.getId())
                    .build();
            pushUpRepository.save(pushUp);
        } else {
            Gym gym = Gym.builder()
                    .groupId(saveExerciseAndCompetitionDto.getGroupId())
                    .exerciseId(savedExercise.getId())
                    .build();
            gymRepository.save(gym);
        }

        return saveExerciseAndCompetitionDto;
    }

    @Override
    public List<WeeklyExerciseDto> getWeeklyResult(ExerciseType exerciseType, Long userId) {
        LocalDate monday = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        List<LocalDate> weekDays = IntStream.range(0, 7).mapToObj(monday::plusDays).toList();

        List<WeeklyExerciseDto> weeklyExerciseDto;
        if (exerciseType == ExerciseType.RUNNING) {
            weeklyExerciseDto = exerciseRepository.getWeeklyRunning(weekDays.get(0), weekDays.get(weekDays.size() - 1), userId);
        } else if (exerciseType == ExerciseType.PUSHUP) {
            weeklyExerciseDto = exerciseRepository.getWeeklyPushUp(weekDays.get(0), weekDays.get(weekDays.size() - 1), userId);
        } else if (exerciseType == ExerciseType.BICYCLING) {
            weeklyExerciseDto = exerciseRepository.getWeeklyBicycling(weekDays.get(0), weekDays.get(weekDays.size() - 1), userId);
        } else {
            weeklyExerciseDto = exerciseRepository.getWeeklyGym(weekDays.get(0), weekDays.get(weekDays.size() - 1), userId);
        }

        return weeklyExerciseDto;
    }
}
