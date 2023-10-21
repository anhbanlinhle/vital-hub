package com.main.server.repository;

import com.main.server.entity.WorkoutMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface WorkoutMappingRepository extends JpaRepository<WorkoutMapping, Long> {

    @Query("SELECT w.suggestGroupId FROM WorkoutMapping w WHERE w.bmiLowerBound < :bmi AND :bmi <= w.bmiUpperBound")
    Long findInRange(Float bmi);
}
