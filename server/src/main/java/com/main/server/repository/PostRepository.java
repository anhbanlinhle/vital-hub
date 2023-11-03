package com.main.server.repository;

import com.main.server.entity.Post;
import com.main.server.utils.dto.PostDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query(value = "(SELECT p.user_id AS userId, p.id AS postId, u.name AS username, p.created_at AS createdAt, p.image AS image, " +
            "p.title AS title, u.avatar AS avatar, IF(:currentUserId = p.user_id, TRUE, FALSE) AS isOwnedInt FROM post p JOIN user u ON p.user_id = u.id " +
            "WHERE p.is_deleted = FALSE AND p.user_id IN :friendList) " +
            "UNION " +
            "(SELECT p.user_id AS userId, p.id AS postId, u.name AS username, p.created_at AS createdAt, p.image AS image, " +
            "p.title AS title, u.avatar AS avatar, IF(:currentUserId = p.user_id, TRUE, FALSE) AS isOwnedInt FROM post p JOIN user u ON p.user_id = u.id " +
            "WHERE p.is_deleted = FALSE AND p.user_id NOT IN :friendList) " +
            "ORDER BY createdAt " +
            "LIMIT :pageSize OFFSET :page", nativeQuery = true)
    List<PostDto> allPostOrderByCreatedTime(Integer page, Integer pageSize, List<Long> friendList, Long currentUserId);

    @Query("SELECT CASE WHEN p.userId = :userId THEN 1 ELSE 0 END AS isOwnedInt, p.userId AS userId, p.id AS postId, u.name AS username, p.title AS title, p.createdAt AS createdAt, u.avatar AS avatar, p.image AS image FROM Post p JOIN User u " +
            "ON p.userId = u.id WHERE p.isDeleted = FALSE AND p.id = :id")
    Optional<PostDto> getPostWithUserByPid(Long id, Long userId);

    Optional<Post> findByIdAndIsDeletedFalse(Long id);
}