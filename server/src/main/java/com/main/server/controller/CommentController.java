package com.main.server.controller;

import com.main.server.entity.Comment;
import com.main.server.middleware.TokenParser;
import com.main.server.service.CommentService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    final CommentService commentService;

    @Autowired
    final TokenParser tokenParser;

    @GetMapping("/by-post")
    public ResponseEntity<?> getCommentsInPost(@RequestParam(name = "page") Integer page,
                                               @RequestParam(name = "pageSize") Integer pageSize,
                                               @RequestParam(name = "postId") Long postId,
                                               @RequestHeader(name = "Authorization") String token) {
        return ResponseEntity.ok().body(commentService.commentsInPost(postId, page, pageSize, tokenParser.getCurrentUserId(token)));
    }

    @PostMapping("/add-comment")
    public ResponseEntity<?> addComment(@RequestBody Comment comment,
                                        @RequestHeader(name = "Authorization") String token) {
        commentService.addComment(tokenParser.getCurrentUserId(token), comment);
        return ResponseEntity.ok().body(null);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<?> addComment(@RequestParam(name = "id") Long id) {
        Comment comment = commentService.deleteComment(id);
        if (comment == null) {
            return ResponseEntity.badRequest().body(null);
        } else {
            return ResponseEntity.ok().body(comment);
        }
    }
}
