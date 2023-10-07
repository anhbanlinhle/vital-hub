package com.main.server.repository;

import com.main.server.entity.WorkoutHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkoutHistoryRepository extends JpaRepository<WorkoutHistory, Long> {

}
