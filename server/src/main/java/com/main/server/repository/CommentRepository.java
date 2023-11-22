package com.main.server.repository;

import com.main.server.entity.Comment;
import com.main.server.utils.dto.CommentDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query(value = """
            SELECT c.id AS id, c.user_id AS userId, c.content AS content, u.avatar AS avatar,
            u.name AS profileName, c.created_at AS createdAt, c.updated_at AS updatedAt,
            IF(c.user_id = :currentUserId, 1, 0) AS isOwnedInt FROM comment c JOIN user u ON c.user_id = u.id
            WHERE c.is_deleted = FALSE AND c.post_id = :postId ORDER BY c.updated_at DESC, c.created_at DESC LIMIT :limit OFFSET :offset
            """
            , nativeQuery = true)
    List<CommentDto> commentsInPost(Long postId, Long currentUserId, Integer limit, Integer offset);

    Optional<Comment> findByIdAndIsDeletedFalse(Long id);
}