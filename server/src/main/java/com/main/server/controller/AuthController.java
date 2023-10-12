package com.main.server.controller;

import com.main.server.service.AuthService;
import com.main.server.service.UserService;
import com.main.server.utils.dto.FirstSignDto;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    final AuthService authService;

    @GetMapping("/sign-in")
    public ResponseEntity<?> test(@RequestHeader("Authorization") String token) {
        if (token == null) {
            throw new AccessDeniedException("No auth");
        }
        token = token.substring(7);
        return authService.signInResponse(token);
    }
}
