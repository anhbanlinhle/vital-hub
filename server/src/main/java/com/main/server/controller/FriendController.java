package com.main.server.controller;

import com.main.server.response.BaseResponse;
import com.main.server.service.FriendService;
import com.main.server.utils.dto.FriendListDto;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/friend")
public class FriendController {

    @Autowired
    private final FriendService friendService;

    @GetMapping("/total")
    public ResponseEntity<BaseResponse> countFriend(@RequestParam Long id) {
        try {
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
    public ResponseEntity<BaseResponse> getFriendList(@RequestParam Long id, @RequestParam @Nullable Integer limit, @RequestParam @Nullable Integer offset) {

        if (limit == null) {
            limit = 10;
        }
        if (offset == null) {
            offset = 0;
        }

        List<FriendListDto> friendList = friendService.getFriendList(id, limit, offset);
        return ResponseEntity.ok().body(BaseResponse.builder()
                .message("success")
                .success(true)
                .data(friendList)
                .build());

    }

}
