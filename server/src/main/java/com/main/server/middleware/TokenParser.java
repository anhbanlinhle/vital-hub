package com.main.server.middleware;

import com.main.server.repository.UserRepository;
import com.main.server.security.JwtService;
import lombok.AllArgsConstructor;
<<<<<<< HEAD
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@AllArgsConstructor
=======
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
>>>>>>> main
@Service
public class TokenParser {

    @Autowired
    final JwtService jwtService;

    @Autowired
    final UserRepository userRepository;

    public Long getCurrentUserId(String token) {
        try {
            token = token.substring(7);
            String gmail = jwtService.extractUsername(token);
            return userRepository.idByGmail(gmail);
        } catch (Exception e) {
            throw e;
        }
    }
}
