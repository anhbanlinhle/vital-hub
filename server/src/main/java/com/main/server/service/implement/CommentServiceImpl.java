package com.main.server.service.implement;

import com.main.server.entity.Comment;
import com.main.server.repository.CommentRepository;
import com.main.server.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public List<Comment> commentsInPost(Long postId, Integer page, Integer pageSize) {
        Page<Comment> comments = commentRepository.commentsInPost(PageRequest.of(page, pageSize), postId);
        return comments.getContent();
    }

    @Override
    public Comment addComment(Long userId, Comment comment) {
        comment.setUserId(userId);
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUpdatedAt(LocalDateTime.now());
        comment.setIsDeleted(false);
        return commentRepository.save(comment);
    }
}
