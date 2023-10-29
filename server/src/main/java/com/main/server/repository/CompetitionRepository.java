package com.main.server.repository;

import com.main.server.entity.Competition;
import com.main.server.utils.dto.CompetitionListDto;
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
                            u.id AS hostId,
                            u.name AS hostName,
                            u.avatar AS hostAvatar,
                            COUNT(DISTINCT p.participant_id) AS participantCount
                        FROM competition c
                        LEFT JOIN participants p ON c.id = p.comp_id
                        LEFT JOIN user u ON c.host_id = u.id
                        WHERE ((c.id IN (SELECT comp_id FROM participants WHERE participant_id = :id) AND c.host_id != :id) = :isJoined)
                        AND IF(:name IS NOT NULL, c.title LIKE CONCAT('%', :name, '%'), 1)
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
                            u.id AS hostId,
                            u.name AS hostName,
                            u.avatar AS hostAvatar,
                            COUNT(DISTINCT p.participant_id) AS participantCount
                        FROM competition c
                        LEFT JOIN participants p ON c.id = p.comp_id
                        LEFT JOIN user u ON c.host_id = u.id
                        WHERE c.host_id = :id
                        AND IF(:name IS NOT NULL, c.title LIKE CONCAT('%', :name, '%'), 1)
                        GROUP BY c.id, c.title, c.background, c.ended_at, u.id, u.name, u.avatar
                        ORDER BY remainDay DESC
                        LIMIT :limit OFFSET :offset
                    """
    )
    List<CompetitionListDto> getOwnCompetitionList(@Param("id") Long id, @Param("name") String name, @Param("limit") Integer limit, @Param("offset") Integer offset);

}
