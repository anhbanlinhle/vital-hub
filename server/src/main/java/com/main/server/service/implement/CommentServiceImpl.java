package com.main.server.service.implement;

import com.main.server.entity.Comment;
import com.main.server.repository.CommentRepository;
import com.main.server.service.CommentService;
import com.main.server.utils.dto.CommentDto;
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
    public List<CommentDto> commentsInPost(Long postId, Integer page, Integer pageSize, Long currentUserId) {
        Page<CommentDto> comments = commentRepository.commentsInPost(PageRequest.of(page, pageSize), postId, currentUserId);
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

    @Override
    public Comment deleteComment(Long id) {
        Comment comment = commentRepository.findByIdAndIsDeletedFalse(id).orElse(null);
        if (comment == null) {
            return null;
        } else {
            comment.setIsDeleted(true);
            commentRepository.save(comment);
            return comment;
        }
    }
}
