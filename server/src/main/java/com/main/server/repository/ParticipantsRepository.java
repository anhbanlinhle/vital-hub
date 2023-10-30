package com.main.server.repository;

import com.main.server.entity.Participants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ParticipantsRepository extends JpaRepository<Participants, Long> {

    @Query(
            nativeQuery = true,
            value = """
                        SELECT EXISTS(SELECT * FROM participants WHERE comp_id = :compId AND participant_id = :currentUserId)
                    """
    )
    boolean existsByCompIdAndParticipantId(Long compId, Long currentUserId);

    @Query(
            nativeQuery = true,
            value = """
                        DELETE FROM participants WHERE comp_id = :compId AND participant_id = :currentUserId
                    """
    )
    void deleteByCompIdAndParticipantId(Long compId, Long currentUserId);
}
