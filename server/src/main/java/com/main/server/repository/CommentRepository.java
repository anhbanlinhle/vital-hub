package com.main.server.repository;

import com.main.server.entity.Comment;
import com.main.server.utils.dto.CommentDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT c.id AS id, c.userId AS userId, c.content AS content, u.avatar AS avatar, u.name AS profileName, c.createdAt AS createdAt, c.updatedAt AS updatedAt FROM Comment c JOIN User u ON c.userId = u.id " +
            "WHERE c.isDeleted = FALSE AND c.postId = :postId ORDER BY c.updatedAt, c.createdAt")
    Page<CommentDto> commentsInPost(Pageable pageable, Long postId);
}