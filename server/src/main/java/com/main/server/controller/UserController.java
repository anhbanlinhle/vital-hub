package com.main.server.controller;

import com.main.server.entity.User;
import com.main.server.middleware.CamelCaseMiddleware;
import com.main.server.middleware.TokenParser;
import com.main.server.request.CheckObjRequest;
import com.main.server.request.UserDetailRequest;
import com.main.server.request.UserInfoRequest;
import com.main.server.response.BaseResponse;
import com.main.server.service.UserService;
import com.main.server.utils.enums.Sex;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    @Autowired
    private final UserService userService;
    @Autowired
    private final TokenParser tokenParser;

    @GetMapping("/test/single-user")
    public ResponseEntity<?> test1() {
        User user = new User();
        user.setId(1L);
        user.setName("tuan");

        return ResponseEntity.ok().body(user);
    }

    @GetMapping("/test/list-user")
    public ResponseEntity<?> test2() {
        List<User> users = new ArrayList<>();

        User user = new User();
        user.setId(100L);
        user.setUserDetail(null);
        user.setSex(Sex.MALE);
        user.setName("tuan dep trai");
        user.setAvatar("111111111111");
        user.setPhoneNo("0192401124");
        user.setGmail("tuan@123");
        users.add(user);

        User user2 = new User();
        user2.setId(102L);
        user2.setUserDetail(null);
        user2.setSex(Sex.MALE);
        user2.setName("tuan dep trai");
        user2.setAvatar("111111111111");
        user2.setPhoneNo("0192401124");
        user2.setGmail("tuan@123");
        users.add(user2);

        User user3 = new User();
        user3.setId(103L);
        user3.setUserDetail(null);
        user3.setSex(Sex.MALE);
        user3.setName("tuan dep trai");
        user3.setAvatar("111111111111");
        user3.setPhoneNo("0192401124");
        user3.setGmail("tuan@123");
        users.add(user3);

        return ResponseEntity.ok().body(users);
    }

    @PostMapping("/add-user-info")
    public ResponseEntity<BaseResponse> addUser(@RequestBody UserInfoRequest request) {
        try {
            userService.createUser(request);
            JSONObject data = new JSONObject();
            data.put("user", request);
            return ResponseEntity.ok().body(BaseResponse.builder()
                    .message("success")
                    .success(true)
                    .data(data)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(BaseResponse.builder()
                    .message(e.getMessage())
                    .success(false)
                    .data(null)
                    .build());
        }
    }

    @PutMapping("/update-user-info")
    public ResponseEntity<BaseResponse> updateUser(@RequestBody UserInfoRequest request) {
        try {
            userService.updateUser(request);
            JSONObject data = new JSONObject();
            data.put("user", request);
            return ResponseEntity.ok().body(BaseResponse.builder()
                    .message("success")
                    .success(true)
                    .data(data)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(BaseResponse.builder()
                    .message(e.getMessage())
                    .success(false)
                    .data(null)
                    .build());
        }
    }

    @GetMapping("/detail")
    public ResponseEntity<BaseResponse> getUserDetail(@RequestHeader("Authorization") String token, @Nullable @RequestParam Long id) {
        try {
            if (id == null) {
                id = tokenParser.getCurrentUserId(token);
            }

            return ResponseEntity.ok().body(BaseResponse.builder()
                    .message("Get user detail success")
                    .success(true)
                    .data(userService.getUserDetailById(id))
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(BaseResponse.builder()
                    .message(e.getMessage())
                    .success(false)
                    .data(null)
                    .build());
        }
    }

    @GetMapping("/info")
    public ResponseEntity<BaseResponse> getUserInfo(@RequestHeader("Authorization") String token, @Nullable @RequestParam Long id) {
        try {
            Long self_id = tokenParser.getCurrentUserId(token);
            if (id == null) {
                id = tokenParser.getCurrentUserId(token);
            }
            return ResponseEntity.ok().body(BaseResponse.builder()
                    .message("Get user info success")
                    .success(true)
                    .data(userService.getUserDtoById(self_id, id))
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(BaseResponse.builder()
                    .message(e.getMessage())
                    .success(false)
                    .data(null)
                    .build());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<BaseResponse> searchUser(@RequestHeader("Authorization") String token, @Nullable @RequestParam String name, @Nullable @RequestParam Integer limit, @Nullable @RequestParam Integer offset) {
        try {
            Long self_id = tokenParser.getCurrentUserId(token);
            if (limit == null) {
                limit = 10;
            }
            if (offset == null) {
                offset = 0;
            }
            return ResponseEntity.ok().body(BaseResponse.builder()
                    .message("Get user info success")
                    .success(true)
                    .data(userService.findUser(self_id, name, limit, offset))
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(BaseResponse.builder()
                    .message(e.getMessage())
                    .success(false)
                    .data(null)
                    .build());
        }
    }

    @PutMapping("/save-detail")
public ResponseEntity<BaseResponse> saveUserDetail(@RequestHeader("Authorization") String token, @RequestBody UserDetailRequest request) {
        try {
            Long self_id = tokenParser.getCurrentUserId(token);
            userService.saveUserDetail(self_id, request);
            return ResponseEntity.ok().body(BaseResponse.builder()
                    .message("Save user detail success")
                    .success(true)
                    .data(request)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(BaseResponse.builder()
                    .message(e.getMessage())
                    .success(false)
                    .data(null)
                    .build());
        }
    }

    @GetMapping("/test-header")
    public ResponseEntity<?> getHeader(@RequestHeader("Authorization") Object authorization) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("data", authorization == null ? "fail" : authorization);
        return ResponseEntity.ok().body(jsonObject);
    }
}
