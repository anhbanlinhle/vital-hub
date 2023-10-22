package com.main.server.controller;

import com.main.server.middleware.TokenParser;
import com.main.server.repository.CompetitionRepository;
import com.main.server.response.BaseResponse;
import com.main.server.service.CompetitionService;
import com.main.server.service.FriendService;
import com.main.server.utils.dto.CompetitionListDto;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
