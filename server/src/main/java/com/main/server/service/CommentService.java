package com.main.server.service;

import com.main.server.entity.Comment;
import com.main.server.utils.dto.CommentDto;

import java.util.List;

public interface CommentService {

    List<CommentDto> commentsInPost(Long postId, Integer page, Integer pageSize, Long currentUserId);

    Comment addComment(Long userId, Comment comment);

    Comment deleteComment(Long id);
}
