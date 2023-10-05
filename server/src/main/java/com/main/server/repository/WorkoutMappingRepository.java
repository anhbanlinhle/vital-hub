package com.main.server.repository;

import com.main.server.entity.WorkoutMapping;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkoutMappingRepository extends JpaRepository<WorkoutMapping, Long> {

}
