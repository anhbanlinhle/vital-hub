package com.main.server.controller;

import com.main.server.middleware.TokenParser;
import com.main.server.response.BaseResponse;
import com.main.server.service.FriendService;
import com.main.server.utils.dto.UserDto;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/friend")
public class FriendController {

    @Autowired
    private final FriendService friendService;

    @Autowired
    private final TokenParser tokenParser;


    @GetMapping("/total")
    public ResponseEntity<BaseResponse> countFriend(HttpServletRequest request) {
        try {
            Long id = tokenParser.getCurrentUserId(request.getHeader("Authorization"));
            int totalFriend = friendService.countFriend(id);
            return ResponseEntity.ok().body(BaseResponse.builder()
                    .message("success")
                    .success(true)
                    .data(totalFriend)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.ok().body(BaseResponse.builder()
                    .message(e.getMessage())
                    .success(false)
                    .data(null)
                    .build());
        }
    }

    @GetMapping("/list")
    public ResponseEntity<BaseResponse> getFriendList(HttpServletRequest request, @RequestParam @Nullable String name, @RequestParam @Nullable Integer limit, @RequestParam @Nullable Integer offset) {
        Long currentUserId = tokenParser.getCurrentUserId(request.getHeader("Authorization"));
        if (limit == null) {
            limit = 10;
        }
        if (offset == null) {
            offset = 0;
        }

        List<UserDto> friendList = friendService.getFriendList(currentUserId, name, limit, offset);
        return ResponseEntity.ok().body(BaseResponse.builder()
                .message("success")
                .success(true)
                .data(friendList)
                .build());

    }

    @PostMapping("/add")
    public ResponseEntity<BaseResponse> addFriend(@RequestHeader("Authorization") String token, @RequestParam Long id) {
        Long currentUserId = tokenParser.getCurrentUserId(token);
        friendService.addFriend(currentUserId, id);
        return ResponseEntity.ok().body(BaseResponse.builder()
                .message("Add friend success")
                .success(true)
                .build());
    }

    @DeleteMapping("/delete")
    public ResponseEntity<BaseResponse> deleteFriend(@RequestHeader("Authorization") String token, @RequestParam Long id) {
        Long currentUserId = tokenParser.getCurrentUserId(token);
        friendService.deleteFriend(currentUserId, id);
        return ResponseEntity.ok().body(BaseResponse.builder()
                .message("Delete friend success")
                .success(true)
                .build());
    }

    @PutMapping("/accept")
    public ResponseEntity<BaseResponse> acceptFriend(@RequestHeader("Authorization") String token, @RequestParam Long id) {
        Long currentUserId = tokenParser.getCurrentUserId(token);
        friendService.acceptFriend(currentUserId, id);
        return ResponseEntity.ok().body(BaseResponse.builder()
                .message("Accept friend success")
                .success(true)
                .build());
    }
}
