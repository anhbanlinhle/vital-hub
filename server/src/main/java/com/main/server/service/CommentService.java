package com.main.server.service;

import com.main.server.entity.Comment;

import java.util.List;

public interface CommentService {

    List<Comment> commentsInPost(Long postId, Integer page, Integer pageSize);

    Comment addComment(Long userId, Comment comment);
}
