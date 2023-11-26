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
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Exercise exercise = Exercise.builder()
                .userId(userId)
                .startedAt(LocalDateTime.parse(saveExerciseAndCompetitionDto.getStartedAt(), formatter))
                .endedAt(LocalDateTime.now())
                .calo(saveExerciseAndCompetitionDto.getCalo())
                .type(saveExerciseAndCompetitionDto.getType())
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
    public Map<ExerciseType, List<WeeklyExerciseDto>> getWeeklyResult(Long userId) {
        LocalDate monday = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        List<LocalDate> weekDays = IntStream.range(0, 7).mapToObj(monday::plusDays).toList();

        Map<ExerciseType, List<WeeklyExerciseDto>> resultExerciseMap = new HashMap<>();


        resultExerciseMap.put(ExerciseType.RUNNING, exerciseRepository.getWeeklyRunning(weekDays.get(0), weekDays.get(weekDays.size() - 1), userId));
        resultExerciseMap.put(ExerciseType.BICYCLING, exerciseRepository.getWeeklyBicycling(weekDays.get(0), weekDays.get(weekDays.size() - 1), userId));
        resultExerciseMap.put(ExerciseType.GYM, exerciseRepository.getWeeklyGym(weekDays.get(0), weekDays.get(weekDays.size() - 1), userId));
        resultExerciseMap.put(ExerciseType.PUSHUP, exerciseRepository.getWeeklyPushUp(weekDays.get(0), weekDays.get(weekDays.size() - 1), userId));

        for (ExerciseType exerciseType : resultExerciseMap.keySet()) {
            List<WeeklyExerciseDto> weeklyExerciseDto = resultExerciseMap.get(exerciseType);
            int resultIndex = 0;
            int weekDayIndex = 0;

            while (weekDayIndex < weekDays.size()) {
                try {
                    if (!weekDays.get(weekDayIndex).equals(weeklyExerciseDto.get(resultIndex).getDate())) {
                        weeklyExerciseDto.add(resultIndex, noExerciseDay(exerciseType, weekDays.get(weekDayIndex)));
                    }
                } catch (IndexOutOfBoundsException e) {
                    weeklyExerciseDto.add(noExerciseDay(exerciseType, weekDays.get(weekDayIndex)));
                }
                resultIndex++;
                weekDayIndex++;
            }
        }
        return resultExerciseMap;
    }

    private WeeklyExerciseDto noExerciseDay(ExerciseType exerciseType, LocalDate localDate) {
        return new WeeklyExerciseDto() {
            @Override
            public Float getCalo() {
                return 0F;
            }

            @Override
            public Float getDistance() {
                return exerciseType == ExerciseType.BICYCLING ? 0F : null;
            }

            @Override
            public Integer getGymGroup() {
                return exerciseType == ExerciseType.GYM ? 0 : null;
            }

            @Override
            public Integer getRep() {
                return exerciseType == ExerciseType.PUSHUP ? 0 : null;
            }

            @Override
            public Integer getStep() {
                return exerciseType == ExerciseType.RUNNING ? 0 : null;
            }

            @Override
            public Integer getTotalTime() {
                return 0;
            }

            @Override
            public LocalDate getDate() {
                return localDate;
            }
        };
    }
}
