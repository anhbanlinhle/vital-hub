package com.main.server.repository;

import com.main.server.entity.Participants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ParticipantsRepository extends JpaRepository<Participants, Long> {

    Boolean existsByCompIdAndParticipantId(Long compId, Long currentUserId);

    void deleteByCompIdAndParticipantId(Long compId, Long currentUserId);
}
