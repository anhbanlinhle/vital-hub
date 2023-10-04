package com.main.server.controller;

import com.main.server.security.JwtService;
import com.main.server.security.TokenResponse;
import com.main.server.utils.dto.OauthResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    JwtService jwtService;

    @GetMapping("/sign-in")
    public ResponseEntity<?> test(@RequestParam(name = "token") String token) {
        RestTemplate restTemplate = new RestTemplate();
        String uri = "https://oauth2.googleapis.com/tokeninfo?id_token=" + token;
        OauthResponse object = null;
        try {
            object = restTemplate.getForObject(uri, OauthResponse.class);
            if (object == null) {
                throw new HttpClientErrorException(HttpStatusCode.valueOf(400));
            }
        } catch (HttpClientErrorException exception) {
            exception.printStackTrace();
            return ResponseEntity.badRequest().body("Invalid token");
        }

        return ResponseEntity.ok().body(new TokenResponse(jwtService.generateToken(object.getEmail())));
    }
}
