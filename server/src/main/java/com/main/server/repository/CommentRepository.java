package com.main.server.repository;

import com.main.server.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT c FROM Comment c WHERE c.isDeleted = FALSE AND c.postId = :postId ORDER BY c.updatedAt, c.createdAt")
    Page<Comment> commentsInPost(Pageable pageable, Long postId);
}
