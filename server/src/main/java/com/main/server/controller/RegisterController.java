package com.main.server.controller;

import com.main.server.service.UserService;
import com.main.server.utils.dto.FirstSignDto;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/register")
@AllArgsConstructor
public class RegisterController {
    final UserService userService;

    @PostMapping("/create-user-first-sign")
    public ResponseEntity<?> createNewUser(@RequestBody FirstSignDto firstSignDto) {
        userService.createUser(firstSignDto);
        return ResponseEntity.ok().body(null);
    }
}
