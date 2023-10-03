package com.main.server.controller;

import com.main.server.utils.dto.OauthResponse;
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

    @GetMapping("/sign-in")
    public ResponseEntity<?> test(@RequestParam(name = "token") String token) {
        RestTemplate restTemplate = new RestTemplate();
        String uri = "https://oauth2.googleapis.com/tokeninfo?id_token=" + token;
        OauthResponse object = null;
        try {
            object = restTemplate.getForObject(uri, OauthResponse.class);
        } catch (HttpClientErrorException exception) {
            exception.printStackTrace();
            return ResponseEntity.badRequest().body("Invalid token");
        }

        return ResponseEntity.ok().body(object);
    }
}
