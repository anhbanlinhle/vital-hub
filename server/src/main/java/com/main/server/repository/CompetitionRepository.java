package com.main.server.repository;

import com.main.server.entity.Competition;
import com.main.server.utils.dto.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalTime;
import java.util.List;

public interface CompetitionRepository extends JpaRepository<Competition, Long> {

    @Query(
            nativeQuery = true,
            value = """
                        SELECT
                            c.id AS id,
                            c.title AS title,
                            c.background AS background,
                            TIMESTAMPDIFF(DAY, NOW(), c.ended_at) AS remainDay,
                            IF(TIMESTAMPDIFF(SECOND, NOW(), c.ended_at) < 0, FALSE, TRUE) AS isOngoing,
                            c.type AS type,
                            u.id AS hostId,
                            u.name AS hostName,
                            u.avatar AS hostAvatar,
                            COUNT(DISTINCT p.participant_id) AS participantCount
                        FROM competition c
                        LEFT JOIN participants p ON c.id = p.comp_id
                        LEFT JOIN user u ON c.host_id = u.id
                        WHERE ((c.id IN (SELECT comp_id FROM participants WHERE participant_id = :id)) = :isJoined)
                        AND IF(:name IS NOT NULL, c.title LIKE CONCAT('%', :name, '%'), 1) AND c.is_deleted = FALSE
                        GROUP BY c.id, c.title, c.background, c.ended_at, u.id, u.name, u.avatar
                        ORDER BY c.ended_at DESC, c.id DESC
                        LIMIT :limit OFFSET :offset
                    """
    )
    public List<CompetitionListDto> getCompetitionList(@Param("isJoined") Boolean isJoined, @Param("id") Long id, @Param("name") String name, @Param("limit") Integer limit, @Param("offset") Integer offset);

    @Query(
            nativeQuery = true,
            value = """
                        SELECT
                            c.id AS id,
                            c.title AS title,
                            c.background AS background,
                            TIMESTAMPDIFF(DAY, NOW(), c.ended_at) AS remainDay,
                            IF(TIMESTAMPDIFF(SECOND, NOW(), c.ended_at) < 0, FALSE, TRUE) AS isOngoing,
                            c.type AS type,
                            u.id AS hostId,
                            u.name AS hostName,
                            u.avatar AS hostAvatar,
                            COUNT(DISTINCT p.participant_id) AS participantCount
                        FROM competition c
                        LEFT JOIN participants p ON c.id = p.comp_id
                        LEFT JOIN user u ON c.host_id = u.id
                        WHERE c.host_id = :id AND c.is_deleted = FALSE
                        AND IF(:name IS NOT NULL, c.title LIKE CONCAT('%', :name, '%'), 1)
                        GROUP BY c.id, c.title, c.background, c.ended_at, u.id, u.name, u.avatar
                        ORDER BY c.ended_at DESC, c.id DESC
                        LIMIT :limit OFFSET :offset
                    """
    )
    List<CompetitionListDto> getOwnCompetitionList(@Param("id") Long id, @Param("name") String name, @Param("limit") Integer limit, @Param("offset") Integer offset);

    Competition findByIdAndIsDeletedFalse(Long id);

    @Query("SELECT COUNT(p.participantId) AS participants, c.id AS id, u.name AS host, CONCAT(c.startedAt, ' - ', c.endedAt) AS time ," +
            "c.title AS title, c.type AS type, c.background AS background, CONCAT(c.duration, '') AS duration, c.hostId AS hostId " +
            "FROM Participants p LEFT JOIN Competition c ON p.compId = c.id JOIN User u ON c.hostId = u.id " +
            "WHERE c.id = :id GROUP BY p.compId")
    CompetitionDetailDto getCompetitionDetail(Long id);

    @Query(value = "SELECT u.name AS name, u.avatar AS avatar, CONCAT(r.step, ' steps') AS score " +
            "FROM compe_ex ce JOIN exercise e on ce.exercise_id = e.id JOIN running r on e.id = r.exercise_id JOIN user u on e.user_id = u.id " +
            "WHERE ce.compe_id = :id ORDER BY r.step DESC LIMIT 3",
            nativeQuery = true)
    List<CompetitionRankingDto> getCompetitionRunningRanking(Long id);

    @Query(value = "SELECT u.name AS name, u.avatar AS avatar, CONCAT(r.distance, ' meters') AS score " +
            "FROM compe_ex ce JOIN exercise e on ce.exercise_id = e.id JOIN bicycling r on e.id = r.exercise_id JOIN user u on e.user_id = u.id " +
            "WHERE ce.compe_id = :id ORDER BY r.distance DESC LIMIT 3",
            nativeQuery = true)
    List<CompetitionRankingDto> getCompetitionBicyclingRanking(Long id);

    @Query(value = "SELECT u.name AS name, u.avatar AS avatar, CONCAT(r.rep, ' reps') AS score " +
            "FROM compe_ex ce JOIN exercise e on ce.exercise_id = e.id JOIN push_up r on e.id = r.exercise_id JOIN user u on e.user_id = u.id " +
            "WHERE ce.compe_id = :id ORDER BY r.rep DESC LIMIT 3",
            nativeQuery = true)
    List<CompetitionRankingDto> getCompetitionPushUpRanking(Long id);

    @Query(value = "SELECT * FROM competition WHERE host_id = :currentUserId ORDER BY created_at DESC LIMIT 1",
            nativeQuery = true)
    CompetitionListDto findFirstByHostIdOrderByCreatedAtDesc(Long currentUserId);

    @Query(value = """
            (SELECT * FROM
            ((WITH
                ec AS
                (SELECT c.id AS competitionId, c.title AS title, c.background AS background, c.ended_at AS endedAt, c.type AS type, (SELECT COUNT(p2.participant_id) FROM participants p2 WHERE p2.comp_id = c.id) AS participants
                FROM participants p LEFT JOIN competition c ON c.id = p.comp_id
                WHERE (c.host_id = :uid OR p.participant_id = :uid) AND c.is_deleted = FALSE
                GROUP BY c.id)
            (SELECT (SELECT COUNT(*) FROM (SELECT r2.distance FROM compe_ex ce2 JOIN exercise e2 ON (ce2.exercise_id = e2.id AND ce2.compe_id = ec.competitionId) JOIN bicycling r2 on e2.id = r2.exercise_id) cc WHERE r.distance <= cc.distance) - (SELECT COUNT(*) FROM (SELECT r3.distance, r3.id FROM compe_ex ce3 JOIN exercise e3 ON (ce3.exercise_id = e3.id AND ce3.compe_id = ec.competitionId) JOIN bicycling r3 on e3.id = r3.exercise_id) cc2 WHERE r.distance = cc2.distance AND r.id != cc2.id) AS position, ec.*, CONCAT(r.distance, ' meters') AS score
            FROM ec LEFT JOIN compe_ex ce ON ec.competitionId = ce.compe_id
            LEFT JOIN exercise e on (ce.exercise_id = e.id)
            LEFT JOIN bicycling r on e.id = r.exercise_id
            WHERE e.user_id = :uid AND ec.type = 'BICYCLING'
            GROUP BY ec.competitionId))
            UNION
            (SELECT 0 AS position, c.id AS competitionId, c.title AS title, c.background AS background, c.ended_at AS endedAt, c.type AS type, (SELECT COUNT(p2.participant_id) FROM participants p2 WHERE p2.comp_id = c.id) AS participants, '0' AS score
            FROM participants p LEFT JOIN competition c ON c.id = p.comp_id
            WHERE ((c.host_id = :uid OR p.participant_id = :uid) AND c.type = 'BICYCLING' AND c.is_deleted = FALSE)
            GROUP BY c.id)) tb
            GROUP BY tb.competitionId)
            UNION
            (SELECT * FROM
            ((WITH
                ec AS
                (SELECT c.id AS competitionId, c.title AS title, c.background AS background, c.ended_at AS endedAt, c.type AS type, (SELECT COUNT(p2.participant_id) FROM participants p2 WHERE p2.comp_id = c.id) AS participants
                FROM participants p LEFT JOIN competition c ON c.id = p.comp_id
                WHERE (c.host_id = :uid OR p.participant_id = :uid) AND c.is_deleted = FALSE
                GROUP BY c.id)
            (SELECT (SELECT COUNT(*) FROM (SELECT r2.step FROM compe_ex ce2 JOIN exercise e2 ON (ce2.exercise_id = e2.id AND ce2.compe_id = ec.competitionId) JOIN running r2 on e2.id = r2.exercise_id) cc WHERE r.step <= cc.step) - (SELECT COUNT(*) FROM (SELECT r3.step, r3.id FROM compe_ex ce3 JOIN exercise e3 ON (ce3.exercise_id = e3.id AND ce3.compe_id = ec.competitionId) JOIN running r3 on e3.id = r3.exercise_id) cc2 WHERE r.step = cc2.step AND r.id != cc2.id) AS position, ec.*, CONCAT(r.step, ' steps') AS score
            FROM ec LEFT JOIN compe_ex ce ON ec.competitionId = ce.compe_id
            LEFT JOIN exercise e on (ce.exercise_id = e.id)
            LEFT JOIN running r on e.id = r.exercise_id
            WHERE e.user_id = :uid AND ec.type = 'RUNNING'
            GROUP BY ec.competitionId))
            UNION
            (SELECT 0 AS position, c.id AS competitionId, c.title AS title, c.background AS background, c.ended_at AS endedAt, c.type AS type, (SELECT COUNT(p2.participant_id) FROM participants p2 WHERE p2.comp_id = c.id) AS participants, '0' AS score
            FROM participants p LEFT JOIN competition c ON c.id = p.comp_id
            WHERE ((c.host_id = :uid OR p.participant_id = :uid) AND c.type = 'RUNNING' AND c.is_deleted = FALSE)
            GROUP BY c.id)) tb
            GROUP BY tb.competitionId)
            UNION
            (SELECT * FROM
            ((WITH
                ec AS
                (SELECT c.id AS competitionId, c.title AS title, c.background AS background, c.ended_at AS endedAt, c.type AS type, (SELECT COUNT(p2.participant_id) FROM participants p2 WHERE p2.comp_id = c.id) AS participants
                FROM participants p LEFT JOIN competition c ON c.id = p.comp_id
                WHERE (c.host_id = :uid OR p.participant_id = :uid) AND c.is_deleted = FALSE
                GROUP BY c.id)
            (SELECT (SELECT COUNT(*) FROM (SELECT r2.rep FROM compe_ex ce2 JOIN exercise e2 ON (ce2.exercise_id = e2.id AND ce2.compe_id = ec.competitionId) JOIN push_up r2 on e2.id = r2.exercise_id) cc WHERE r.rep <= cc.rep) - (SELECT COUNT(*) FROM (SELECT r3.rep, r3.id FROM compe_ex ce3 JOIN exercise e3 ON (ce3.exercise_id = e3.id AND ce3.compe_id = ec.competitionId) JOIN push_up r3 on e3.id = r3.exercise_id) cc2 WHERE r.rep = cc2.rep AND r.id != cc2.id) AS position, ec.*, CONCAT(r.rep, ' reps') AS score
            FROM ec LEFT JOIN compe_ex ce ON ec.competitionId = ce.compe_id
            LEFT JOIN exercise e on (ce.exercise_id = e.id)
            LEFT JOIN push_up r on e.id = r.exercise_id
            WHERE e.user_id = :uid AND ec.type = 'PUSHUP'
            GROUP BY ec.competitionId))
            UNION
            (SELECT 0 AS position, c.id AS competitionId, c.title AS title, c.background AS background, c.ended_at AS endedAt, c.type AS type, (SELECT COUNT(p2.participant_id) FROM participants p2 WHERE p2.comp_id = c.id) AS participants, '0' AS score
            FROM participants p LEFT JOIN competition c ON c.id = p.comp_id
            WHERE ((c.host_id = :uid OR p.participant_id = :uid) AND c.type = 'PUSHUP' AND c.is_deleted = FALSE)
            GROUP BY c.id)) tb
            GROUP BY tb.competitionId)
            LIMIT :limit OFFSET :offset
            """
            , nativeQuery = true)
    List<EnrolledCompetitionDto> getEnrolledCompetition(Long uid, Integer limit, Integer offset);

    @Query(value = """
            SELECT r.step AS step, ce.exercise_id AS exerciseId FROM user u JOIN participants p ON u.id = p.participant_id
            JOIN compe_ex ce ON p.comp_id = ce.compe_id JOIN running r on ce.exercise_id = r.exercise_id JOIN exercise e ON (e.id = ce.exercise_id AND e.user_id = u.id)
            WHERE p.comp_id = :competitionId AND u.id = :userId
            """
            , nativeQuery = true)
    CompetitionResultDto getResultRunningForUser(Long competitionId, Long userId);

    @Query(value = """
            SELECT b.distance AS distance, ce.exercise_id AS exerciseId FROM user u JOIN participants p ON u.id = p.participant_id
            JOIN compe_ex ce ON p.comp_id = ce.compe_id JOIN bicycling b on ce.exercise_id = b.exercise_id JOIN exercise e ON (e.id = ce.exercise_id AND e.user_id = u.id)
            WHERE p.comp_id = :competitionId AND u.id = :userId
            """
            , nativeQuery = true)
    CompetitionResultDto getResultBicyclingForUser(Long competitionId, Long userId);

    @Query(value = """
            SELECT pu.rep AS rep, ce.exercise_id AS exerciseId FROM user u JOIN participants p ON u.id = p.participant_id
            JOIN compe_ex ce ON p.comp_id = ce.compe_id JOIN push_up pu ON ce.exercise_id = pu.exercise_id JOIN exercise e ON (e.id = ce.exercise_id AND e.user_id = u.id)
            WHERE p.comp_id = :competitionId AND u.id = :userId
            """
            , nativeQuery = true)
    CompetitionResultDto getResultPushUpForUser(Long competitionId, Long userId);

    @Query(value = "SELECT id, title FROM competition c JOIN participants p ON c.id = p.comp_id WHERE type = 'RUNNING' AND p.participant_id = :currentUserId",
            nativeQuery = true)
    List<CompeMiniDto> getJoinedCompetitionRunning(Long currentUserId);

    @Query(value = "SELECT id, title FROM competition c JOIN participants p ON c.id = p.comp_id WHERE type = 'PUSHUP' AND p.participant_id = :currentUserId",
            nativeQuery = true)
    List<CompeMiniDto> getJoinedCompetitionPushUp(Long currentUserId);

    @Query(value = "SELECT id, title FROM competition c JOIN participants p ON c.id = p.comp_id WHERE type = 'BICYCLING' AND p.participant_id = :currentUserId",
            nativeQuery = true)
    List<CompeMiniDto> getJoinedCompetitionBicycling(Long currentUserId);

    @Query(value = "SELECT duration FROM competition WHERE id = :id",
            nativeQuery = true)
    LocalTime getDuration(Long id);
}
