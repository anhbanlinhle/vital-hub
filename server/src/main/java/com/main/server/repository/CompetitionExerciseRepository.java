package com.main.server.repository;

import com.main.server.entity.CompeEx;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface CompetitionExerciseRepository extends JpaRepository<CompeEx, Long> {

    @Transactional
    @Modifying
    @Query("UPDATE CompeEx ce SET ce.exerciseId = :newExerciseId WHERE ce.compeId = :competitionId AND ce.exerciseId = :oldExerciseId")
    void updateExerciseForCompetition(Long competitionId, Long oldExerciseId, Long newExerciseId);
}
