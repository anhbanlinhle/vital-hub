package com.main.server.repository;

import com.main.server.entity.CompetitionHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompetitionHistoryRepository extends JpaRepository<CompetitionHistory, Long> {

}
