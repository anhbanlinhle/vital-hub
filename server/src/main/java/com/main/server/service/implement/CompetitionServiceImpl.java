package com.main.server.service.implement;

import com.main.server.entity.Competition;
import com.main.server.entity.Participants;
import com.main.server.repository.CompetitionRepository;
import com.main.server.repository.ParticipantsRepository;
import com.main.server.request.AddCompettitionRequest;
import com.main.server.service.CompetitionService;
import com.main.server.utils.dto.CompetitionListDto;
import com.main.server.utils.enums.ExerciseType;
import com.main.server.utils.dto.*;
import com.main.server.utils.enums.ExerciseType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CompetitionServiceImpl implements CompetitionService {

    private final CompetitionRepository competitionRepository;
    private final ParticipantsRepository participantsRepository;
    @Override
    public List<CompetitionListDto> getCompetitionList(Boolean isJoined, Long id, String name, Integer limit, Integer offset) {
        return competitionRepository.getCompetitionList(isJoined, id, name, limit, offset);
    }

    @Override
    public void joinOrLeaveCompetition(Long currentUserId, Long compId, Boolean joining) {
        if (joining) {
            participantsRepository.save(Participants.builder()
                    .compId(compId)
                    .participantId(currentUserId)
                    .build());
        } else {
            participantsRepository.deleteByCompIdAndParticipantId(compId, currentUserId);
        }
    }

    @Override
    public void addCompetition(Long currentUserId, AddCompettitionRequest request) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        String title = request.getTitle();
        String background = request.getBackground();
        ExerciseType type = ExerciseType.valueOf(request.getType().toUpperCase());
        LocalDateTime startDate = LocalDateTime.parse(request.getStartedAt(), formatter);
        LocalDateTime endDate = LocalDateTime.parse(request.getEndedAt(), formatter);
        LocalTime duration = LocalTime.parse(request.getDuration());

        competitionRepository.save(Competition.builder()
                .title(title)
                .background(background)
                .type(type)
                .createdAt(LocalDateTime.now())
                .startedAt(startDate)
                .endedAt(endDate)
                .duration(duration)
                .hostId(currentUserId)
                .isDeleted(false)
                .build());
        participantsRepository.save(Participants.builder()
                .compId(competitionRepository.findFirstByHostIdOrderByCreatedAtDesc(currentUserId).getId())
                .participantId(currentUserId)
                .build());
    }

    @Override
    public List<EnrolledCompetitionDto> getEnrolledCompetition(Long userId, Integer page, Integer pageSize) {
        List<EnrolledCompetitionDto> enrolledCompetitions = competitionRepository.getEnrolledCompetition(userId, pageSize, page*pageSize);
        for (int i = 0; i < enrolledCompetitions.size(); i++) {
            if (enrolledCompetitions.get(i).getCompetitionId() == null) {
                enrolledCompetitions.remove(i);
                i--;
            }
        }
        return enrolledCompetitions;
    }

    @Override
    public CompetitionAllDetailDto getDetailCompetition(Long id, Long userId) {
        Competition competition = competitionRepository.findByIdAndIsDeletedFalse(id);
        if (competition == null) {
            throw new RuntimeException("Cannot find competition");
        }
        List<CompetitionRankingDto> rank;
        if (competition.getType().equals(ExerciseType.BICYCLING)) {
            rank = competitionRepository.getCompetitionBicyclingRanking(id);
        } else if (competition.getType().equals(ExerciseType.RUNNING)) {
            rank = competitionRepository.getCompetitionRunningRanking(id);
        } else {
            rank = competitionRepository.getCompetitionPushUpRanking(id);
        }
        CompetitionDetailDto competitionDetailDto = competitionRepository.getCompetitionDetail(id);
        return new CompetitionAllDetailDto(rank, competitionDetailDto, userId.equals(competitionDetailDto.getHostId()));
    }

    @Override
    public void deleteCompetition(Long id) {
        Competition competition = competitionRepository.findByIdAndIsDeletedFalse(id);
        if (competition == null) {
            throw new RuntimeException("Cannot find competition");
        }
        competition.setIsDeleted(true);
        competitionRepository.save(competition);
    }

    @Override
    public void editCompetition(CompetitionModifyDto competition) {
        Competition existingCompetition = competitionRepository.findByIdAndIsDeletedFalse(competition.getId());
        if (existingCompetition == null) {
            throw new RuntimeException("Cannot find competition");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if (competition.getDuration() != null) {
            existingCompetition.setDuration(LocalTime.parse(competition.getDuration()));
        }
        if (competition.getStartedAt() != null) {
            existingCompetition.setStartedAt(LocalDateTime.parse(competition.getStartedAt(), formatter));
        }
        if (competition.getEndedAt() != null) {
            existingCompetition.setEndedAt(LocalDateTime.parse(competition.getEndedAt(), formatter));
        }
        if (competition.getTitle() != null) {
            existingCompetition.setTitle(competition.getTitle());
        }
        if (competition.getBackground() != null) {
            existingCompetition.setBackground(competition.getBackground());
        }

        competitionRepository.save(existingCompetition);
    }

    @Override
    public List<CompetitionListDto> getOwnCompetitionList(Long id, String name, Integer limit, Integer offset) {
        return competitionRepository.getOwnCompetitionList(id, name, limit, offset);
    }
}
