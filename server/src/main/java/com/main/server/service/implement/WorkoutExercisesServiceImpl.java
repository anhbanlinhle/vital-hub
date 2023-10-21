package com.main.server.service.implement;

import com.main.server.entity.WorkoutExercises;
import com.main.server.repository.WorkoutExercisesRepository;
import com.main.server.service.WorkoutExercisesService;
import com.main.server.utils.dto.GroupExerciseDto;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class WorkoutExercisesServiceImpl implements WorkoutExercisesService {

    @Autowired
    WorkoutExercisesRepository workoutExercisesRepository;

    @Override
    public List<GroupExerciseDto> getExerciseGroups() {
        return workoutExercisesRepository.getGroupExercise();
    }

    @Override
    public List<WorkoutExercises> getAllExercises(Integer page, Integer pageSize, String order) {
        Page<WorkoutExercises> workoutExercisesPage = workoutExercisesRepository.allExerciseOrderBy(PageRequest.of(page, pageSize, Sort.by(order)));
        return workoutExercisesPage.getContent();
    }

    @Override
    public WorkoutExercises singleExerciseById(Long id) {
        return workoutExercisesRepository.findById(id).orElse(null);
    }

    @Override
    public List<WorkoutExercises> getAllExercisesByGroupId(Long id) {
        return workoutExercisesRepository.allExerciseByGroupId(id);
    }
}
