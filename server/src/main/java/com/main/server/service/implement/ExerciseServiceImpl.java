package com.main.server.service.implement;

import com.main.server.repository.ExerciseRepository;
import com.main.server.service.ExerciseService;
import com.main.server.utils.dto.ExerciseDto;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ExerciseServiceImpl implements ExerciseService {

    @Autowired
    ExerciseRepository exerciseRepository;

    @Override
    public List<ExerciseDto> getAllExerciseByUserId(Long userId, Integer page, Integer pageSize) {
        return exerciseRepository.getAllExerciseWithUid(userId, pageSize, page*pageSize);
    }
}
