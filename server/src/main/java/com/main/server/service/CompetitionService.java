package com.main.server.service;

import com.main.server.entity.Competition;
import com.main.server.request.AddCompettitionRequest;
import com.main.server.utils.dto.CompeMiniDto;
import com.main.server.utils.dto.CompetitionAllDetailDto;
import com.main.server.utils.dto.CompetitionListDto;
import com.main.server.utils.dto.CompetitionModifyDto;
import com.main.server.utils.enums.ExerciseType;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public interface CompetitionService {
    List<CompetitionListDto> getCompetitionList(Boolean isJoined, Long id, String name, Integer limit, Integer offset);
    List<CompetitionListDto> getOwnCompetitionList(Long id, String name, Integer limit, Integer offset);
    void joinOrLeaveCompetition(Long currentUserId, Long compId);

    CompetitionAllDetailDto getDetailCompetition(Long id, Long userId);

    void deleteCompetition(Long id);

    void editCompetition(CompetitionModifyDto competition);
    void addCompetition(Long currentUserId, AddCompettitionRequest request);

    List<CompeMiniDto> getJoinedTitleList(Long currentUserId);
}
