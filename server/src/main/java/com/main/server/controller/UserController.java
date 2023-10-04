package com.main.server.controller;

import com.main.server.entity.User;
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
    public ResponseEntity<?> test() {
        User user = new User();
        user.setId(1L);
        user.setName("tuan");

        return ResponseEntity.ok().body(user);
    }
}
