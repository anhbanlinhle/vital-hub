package com.main.server.controller;

import com.main.server.middleware.TokenParser;
import com.main.server.repository.CompetitionRepository;
import com.main.server.request.AddCompettitionRequest;
import com.main.server.response.BaseResponse;
import com.main.server.service.CompetitionService;
import com.main.server.service.FriendService;
import com.main.server.utils.dto.CompetitionListDto;
import com.main.server.utils.enums.ExerciseType;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/competition")
public class CompetitionController {

    @Autowired
    private final CompetitionService competitionService;

    @Autowired
    private final TokenParser tokenParser;

    @GetMapping("/list")
    public ResponseEntity<BaseResponse> getCompetitionList(HttpServletRequest request,@RequestParam Boolean isJoined, @RequestParam @Nullable String name, @RequestParam @Nullable Integer limit, @RequestParam @Nullable Integer offset) {
        Long currentUserId = tokenParser.getCurrentUserId(request.getHeader("Authorization"));
        if (limit == null) {
            limit = 10;
        }
        if (offset == null) {
            offset = 0;
        }
        List<CompetitionListDto> competitionList = competitionService.getCompetitionList(isJoined, currentUserId, name, limit, offset);
        return ResponseEntity.ok().body(BaseResponse.builder()
                .message("success")
                .success(true)
                .data(competitionList)
                .build());
    }

    @GetMapping("/own-list")
    public ResponseEntity<BaseResponse> getOwnCompetitionList(HttpServletRequest request, @RequestParam @Nullable String name, @RequestParam @Nullable Integer limit, @RequestParam @Nullable Integer offset) {
        Long currentUserId = tokenParser.getCurrentUserId(request.getHeader("Authorization"));
        if (limit == null) {
            limit = 10;
        }
        if (offset == null) {
            offset = 0;
        }
        List<CompetitionListDto> competitionList = competitionService.getOwnCompetitionList(currentUserId, name, limit, offset);
        return ResponseEntity.ok().body(BaseResponse.builder()
                .message("success")
                .success(true)
                .data(competitionList)
                .build());
    }

    @PostMapping("/join_or_leave")
    public ResponseEntity<BaseResponse> joinOrLeaveCompetition(HttpServletRequest request, @RequestParam Long compId) {
        Long currentUserId = tokenParser.getCurrentUserId(request.getHeader("Authorization"));
        competitionService.joinOrLeaveCompetition(currentUserId, compId);
        return ResponseEntity.ok().body(BaseResponse.builder()
                .message("success")
                .success(true)
                .build());
    }

    @PostMapping("/add")
    public ResponseEntity<BaseResponse> addCompetition(HttpServletRequest request, @RequestBody AddCompettitionRequest addCompettitionRequest) {
        Long currentUserId = tokenParser.getCurrentUserId(request.getHeader("Authorization"));
        String title = addCompettitionRequest.getTitle();
        String background = addCompettitionRequest.getBackground();
        ExerciseType type = addCompettitionRequest.getType();
        LocalDateTime startDate = addCompettitionRequest.getStartedAt();
        LocalDateTime endDate = addCompettitionRequest.getEndedAt();
        Time duration = addCompettitionRequest.getDuration();
        competitionService.addCompetition(currentUserId, title, background, type, startDate, endDate, duration);
        return ResponseEntity.ok().body(BaseResponse.builder()
                .message("Add competition successfully")
                .success(true)
                .data(addCompettitionRequest)
                .build());
    }
}
