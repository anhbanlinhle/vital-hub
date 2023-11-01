package com.main.server.repository;

import com.main.server.entity.Competition;
import com.main.server.utils.dto.CompetitionDetailDto;
import com.main.server.utils.dto.CompetitionListDto;
import com.main.server.utils.dto.CompetitionRankingDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
                        ORDER BY remainDay DESC
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
                        ORDER BY remainDay DESC
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
            "FROM compe_ex ce JOIN exercise e on ce.exercise_id = e.id JOIN bycicling r on e.id = r.exercise_id JOIN user u on e.user_id = u.id " +
            "WHERE ce.compe_id = :id ORDER BY r.step DESC LIMIT 3",
            nativeQuery = true)
    List<CompetitionRankingDto> getCompetitionBicyclingRanking(Long id);

    @Query(value = "SELECT u.name AS name, u.avatar AS avatar, CONCAT(r.rep, ' reps') AS score " +
            "FROM compe_ex ce JOIN exercise e on ce.exercise_id = e.id JOIN push_up r on e.id = r.exercise_id JOIN user u on e.user_id = u.id " +
            "WHERE ce.compe_id = :id ORDER BY r.step DESC LIMIT 3",
            nativeQuery = true)
    List<CompetitionRankingDto> getCompetitionPushUpRanking(Long id);

    @Query(value = "SELECT * FROM competition WHERE host_id = :currentUserId ORDER BY created_at DESC LIMIT 1",
            nativeQuery = true)
    CompetitionListDto findFirstByHostIdOrderByCreatedAtDesc(Long currentUserId);
}
