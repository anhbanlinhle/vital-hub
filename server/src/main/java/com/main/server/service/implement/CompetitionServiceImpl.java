package com.main.server.service.implement;

import com.main.server.entity.Competition;
import com.main.server.repository.CompetitionRepository;
import com.main.server.service.CompetitionService;
import com.main.server.utils.dto.CompetitionListDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompetitionServiceImpl implements CompetitionService {

    private final CompetitionRepository competitionRepository;
    @Override
    public List<CompetitionListDto> getCompetitionList(Boolean isJoined, Long id, String name, Integer limit, Integer offset) {
        return competitionRepository.getCompetitionList(isJoined, id, name, limit, offset);
    }
}
