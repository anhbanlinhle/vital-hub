package com.main.server.controller;

import com.main.server.middleware.TokenParser;
import com.main.server.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    @Autowired
    final PostService postService;

    @Autowired
    final TokenParser tokenParser;

    @GetMapping("/all")
    public ResponseEntity<?> getPostsPaging(@RequestParam(name = "page") Integer page,
                                            @RequestParam(name = "pageSize") Integer pageSize,
                                            @RequestHeader(name = "Authorization") String token) {
        return ResponseEntity.ok().body(postService.postByPage(page, pageSize, tokenParser.getCurrentUserId(token)));
    }
}
