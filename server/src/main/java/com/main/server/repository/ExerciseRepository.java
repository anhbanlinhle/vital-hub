package com.main.server.repository;

import com.main.server.entity.Exercise;
import com.main.server.utils.dto.ExerciseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ExerciseRepository extends JpaRepository<Exercise, Long> {

    @Query(value = """
            SELECT e.id AS exerciseId, CONCAT(e.started_at, ' - ', e.ended_at) AS time, e.calo AS calo, e.type AS type, CONCAT(r.step, ' steps') AS score
            FROM exercise e JOIN running r on e.id = r.exercise_id
            WHERE e.user_id = :userId
            UNION
            SELECT e.id AS exerciseId, CONCAT(e.started_at, ' - ', e.ended_at) AS time, e.calo AS calo, e.type AS type, CONCAT(b.distance, ' meters') AS score
            FROM exercise e JOIN bicycling b on e.id = b.exercise_id
            WHERE e.user_id = :userId
            UNION
            SELECT e.id AS exerciseId, CONCAT(e.started_at, ' - ', e.ended_at) AS time, e.calo AS calo, e.type AS type, CONCAT(p.rep, ' reps') AS score
            FROM exercise e JOIN push_up p on e.id = p.exercise_id
            WHERE e.user_id = :userId
            UNION
            SELECT e.id AS exerciseId, CONCAT(e.started_at, ' - ', e.ended_at) AS time, e.calo AS calo, e.type AS type, CONCAT(COUNT(we.id), ' exercises') AS score
            FROM exercise e JOIN gym g on e.id = g.exercise_id JOIN workout_exercises we on g.group_id = we.group_id
            WHERE e.user_id = :userId
            GROUP BY e.id
            ORDER BY exerciseId
            """
            , nativeQuery = true)
    List<ExerciseDto> getAllExerciseWithUid(Long userId, Integer limit, Integer offset);
}
