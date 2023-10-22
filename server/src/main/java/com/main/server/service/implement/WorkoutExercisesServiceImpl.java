package com.main.server.service.implement;

import com.main.server.entity.User;
import com.main.server.entity.WorkoutExercises;
import com.main.server.repository.UserRepository;
import com.main.server.repository.WorkoutExercisesRepository;
import com.main.server.repository.WorkoutMappingRepository;
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

    @Autowired
    UserRepository userRepository;

    @Autowired
    WorkoutMappingRepository workoutMappingRepository;

    @Override
    public List<GroupExerciseDto> getExerciseGroups(Boolean isSuggest, Long userId) {
        if (isSuggest) {
            User user = userRepository.findById(userId).orElse(null);
            if (user == null) {
                throw new RuntimeException("Cannot find user");
            }
            Float weight = user.getUserDetail().getCurrentWeight().floatValue();
            Float height = user.getUserDetail().getCurrentHeight().floatValue()/100;
            Float bmi = weight/(height*height);
            Long groupId = workoutMappingRepository.findInRange(bmi);
            return workoutExercisesRepository.getGroupExerciseByGroupId(groupId);
        }
        return workoutExercisesRepository.getGroupExercise();
    }

    @Override
    public List<WorkoutExercises> getAllExercises(Integer page, Integer pageSize, String order, Boolean desc) {
        Page<WorkoutExercises> workoutExercisesPage = workoutExercisesRepository.allExerciseOrderBy(PageRequest.of(page, pageSize, desc ? Sort.by(order).descending() : Sort.by(order)));
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
