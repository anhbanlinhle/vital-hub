package com.main.server.controller;

import com.main.server.entity.Competition;
import com.main.server.middleware.TokenParser;
import com.main.server.repository.CompetitionRepository;
import com.main.server.request.AddCompettitionRequest;
import com.main.server.response.BaseResponse;
import com.main.server.service.CompetitionService;
import com.main.server.service.FriendService;
import com.main.server.utils.dto.CompeMiniDto;
import com.main.server.utils.dto.CompetitionListDto;
import com.main.server.utils.dto.SaveExerciseAndCompetitionDto;
import com.main.server.utils.enums.ExerciseType;
import com.main.server.utils.dto.CompetitionModifyDto;
import com.main.server.utils.dto.CompetitionModifyDto;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

    @PostMapping("/join-or-leave")
    public ResponseEntity<BaseResponse> joinOrLeaveCompetition(HttpServletRequest request,
                                                               @RequestParam(name = "id") Long compId,
                                                               @RequestParam(name = "joining") Boolean joining) {
        Long currentUserId = tokenParser.getCurrentUserId(request.getHeader("Authorization"));
        competitionService.joinOrLeaveCompetition(currentUserId, compId, joining);
        return ResponseEntity.ok().body(null);
    }

    @PostMapping("/add")
    public ResponseEntity<BaseResponse> addCompetition(HttpServletRequest request, @RequestBody AddCompettitionRequest addCompettitionRequest) {
        Long currentUserId = tokenParser.getCurrentUserId(request.getHeader("Authorization"));
        competitionService.addCompetition(currentUserId, addCompettitionRequest);
        return ResponseEntity.ok().body(BaseResponse.builder()
                .message("Add competition successfully")
                .success(true)
                .data(addCompettitionRequest)
                .build());
    }

    @GetMapping("/detail")
    public ResponseEntity<?> getDetailCompetition(@RequestParam(name = "id") Long id,
                                                  @RequestHeader(name = "Authorization") String token) {
        return ResponseEntity.ok().body(competitionService.getDetailCompetition(id, tokenParser.getCurrentUserId(token)));
    }

    @PutMapping("/delete")
    public ResponseEntity<?> deleteCompetition(@RequestParam(name = "id") Long id) {
        competitionService.deleteCompetition(id);
        return ResponseEntity.ok().body(null);
    }

    @PutMapping("/edit")
    public ResponseEntity<?> editCompetition(@RequestBody CompetitionModifyDto competition) {
        competitionService.editCompetition(competition);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/enrolled")
    public ResponseEntity<?> getEnrolledCompetitions(@RequestParam Integer page,
                                                     @RequestParam Integer pageSize,
                                                     @RequestHeader(name = "Authorization") String token) {
        return ResponseEntity.ok().body(competitionService.getEnrolledCompetition(tokenParser.getCurrentUserId(token), page, pageSize));
    }

    @PostMapping("/save-result")
    public ResponseEntity<?> getEnrolledCompetitions(@RequestBody SaveExerciseAndCompetitionDto saveExerciseAndCompetitionDto,
                                                     @RequestHeader(name = "Authorization") String token) {
        competitionService.saveResultForCompetition(saveExerciseAndCompetitionDto, tokenParser.getCurrentUserId(token));
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/joined-running")
    public ResponseEntity<BaseResponse> getJoinedTitleList(@RequestHeader(name = "Authorization") String token) {
        Long currentUserId = tokenParser.getCurrentUserId(token);
        List<CompeMiniDto> competitionList = competitionService.getJoinedTitleList(currentUserId);
        return ResponseEntity.ok().body(BaseResponse.builder()
                .message("success")
                .success(true)
                .data(competitionService.getJoinedTitleList(currentUserId))
                .build());
    }

    @GetMapping("/get-duration")
    public ResponseEntity<BaseResponse> getDuration(@RequestParam(name = "id") Long id) {
        LocalTime duration = competitionService.getDuration(id);
        return ResponseEntity.ok().body(BaseResponse.builder()
                .message("success")
                .success(true)
                .data(duration)
                .build());
    }

}
