package com.main.server.service;

import com.main.server.security.TokenResponse;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<?> signInResponse(String token);
}
