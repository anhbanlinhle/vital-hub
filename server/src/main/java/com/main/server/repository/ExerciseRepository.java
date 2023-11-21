package com.main.server.repository;

import com.main.server.entity.Exercise;
import com.main.server.utils.dto.ExerciseDto;
import com.main.server.utils.dto.WeeklyExerciseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
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
            LIMIT :limit OFFSET :offset
            """
            , nativeQuery = true)
    List<ExerciseDto> getAllExerciseWithUid(Long userId);
    List<ExerciseDto> getAllExerciseWithUid(Long userId, Integer limit, Integer offset);

    @Query(value = """
            SELECT TRUNCATE(SUM(e.calo), 2) AS calo, SUM(r.step) AS step, SUM(TIMESTAMPDIFF(SECOND, e.started_at, e.ended_at)) AS totalTime
            FROM exercise e JOIN running r on e.id = r.exercise_id
            WHERE e.user_id = :userId AND (:monday <= DATE(started_at) AND DATE(started_at) <= :sunday) AND e.type = 'RUNNING'
            GROUP BY DATE(started_at)
            ORDER BY DATE(started_at)
            """, nativeQuery = true)
    List<WeeklyExerciseDto> getWeeklyRunning(LocalDate monday,
                                             LocalDate sunday,
                                             Long userId);

    @Query(value = """
            SELECT TRUNCATE(SUM(e.calo), 2) AS calo, SUM(p.rep) AS rep, SUM(TIMESTAMPDIFF(SECOND, e.started_at, e.ended_at)) AS totalTime
            FROM exercise e JOIN push_up p on e.id = p.exercise_id
            WHERE e.user_id = :userId AND (:monday <= DATE(started_at) AND DATE(started_at) <= :sunday) AND e.type = 'PUSHUP'
            GROUP BY DATE(started_at)
            ORDER BY DATE(started_at)
            """, nativeQuery = true)
    List<WeeklyExerciseDto> getWeeklyPushUp(LocalDate monday,
                                            LocalDate sunday,
                                            Long userId);

    @Query(value = """
            SELECT TRUNCATE(SUM(e.calo), 2) AS calo, TRUNCATE(SUM(b.distance), 2) AS distance, SUM(TIMESTAMPDIFF(SECOND, e.started_at, e.ended_at)) AS totalTime
            FROM exercise e JOIN bicycling b on e.id = b.exercise_id
            WHERE e.user_id = :userId AND (:monday <= DATE(started_at) AND DATE(started_at) <= :sunday) AND e.type = 'BICYCLING'
            GROUP BY DATE(started_at)
            ORDER BY DATE(started_at)
            """, nativeQuery = true)
    List<WeeklyExerciseDto> getWeeklyBicycling(LocalDate monday,
                                               LocalDate sunday,
                                               Long userId);

    @Query(value = """
            SELECT TRUNCATE(SUM(e.calo), 2) AS calo, GROUP_CONCAT(g.group_id) AS gymGroup, SUM(TIMESTAMPDIFF(SECOND, e.started_at, e.ended_at)) AS totalTime
            FROM exercise e JOIN gym g on e.id = g.exercise_id
            WHERE e.user_id = :userId AND (:monday <= DATE(started_at) AND DATE(started_at) <= :sunday) AND e.type = 'GYM'
            GROUP BY DATE(started_at)
            ORDER BY DATE(started_at)
            """, nativeQuery = true)
    List<WeeklyExerciseDto> getWeeklyGym(LocalDate monday,
                                         LocalDate sunday,
                                         Long userId);
}
