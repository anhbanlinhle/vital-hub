package com.main.server.service;

import com.main.server.entity.Competition;
import com.main.server.utils.dto.CompetitionAllDetailDto;
import com.main.server.utils.dto.CompetitionListDto;

import java.util.List;

public interface CompetitionService {
    List<CompetitionListDto> getCompetitionList(Boolean isJoined, Long id, String name, Integer limit, Integer offset);
    List<CompetitionListDto> getOwnCompetitionList(Long id, String name, Integer limit, Integer offset);
    void joinOrLeaveCompetition(Long currentUserId, Long compId);

    CompetitionAllDetailDto getDetailCompetition(Long id);
}
