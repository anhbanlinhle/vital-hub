package com.main.server.controller;

import com.main.server.entity.Comment;
import com.main.server.entity.Post;
import com.main.server.middleware.TokenParser;
import com.main.server.service.PostService;
import com.main.server.utils.dto.PostDto;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
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

    @GetMapping("")
    public ResponseEntity<?> getPostById(@RequestParam(name = "id") Long id,
                                         @RequestHeader(name = "Authorization") String token) {
        PostDto post = postService.postById(id, tokenParser.getCurrentUserId(token));
        return post != null ? ResponseEntity.ok().body(post) : ResponseEntity.badRequest().body(null);
    }

    @PutMapping("/remove")
    public ResponseEntity<?> addComment(@RequestParam(name = "id") Long id) {
        Post post = postService.deletePost(id);
        if (post == null) {
            return ResponseEntity.badRequest().body(null);
        } else {
            return ResponseEntity.ok().body(post);
        }
    }
}
