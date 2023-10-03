package com.main.server.controller;

import com.main.server.middleware.CamelCaseMiddleware;
import com.main.server.request.CheckObjRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping("/test")
    public ResponseEntity<?> test(@RequestParam(name = "token") String token) {
        RestTemplate restTemplate = new RestTemplate();
        String uri = "https://oauth2.googleapis.com/tokeninfo?id_token=" + token;
        Object object = null;
        try {
            object = restTemplate.getForObject(uri, Object.class);
        } catch (HttpClientErrorException exception) {
            exception.printStackTrace();
            return ResponseEntity.badRequest().body("Invalid token");
        }

        return ResponseEntity.ok().body(object);
    }
}
