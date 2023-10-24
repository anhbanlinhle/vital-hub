package com.main.server.repository;

import com.main.server.entity.Post;
import com.main.server.utils.dto.PostDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query(value = "(SELECT p.user_id AS userId, p.id AS postId, u.name AS username, p.created_at AS createdAt, p.title AS title, user.avatar AS avatar FROM post p JOIN user u ON p.user_id = u.id " +
            "WHERE p.is_deleted = FALSE AND (:noFriend = TRUE OR p.user_id IN :friendList)) " +
            "UNION " +
            "(SELECT p.user_id AS userId, p.id AS postId, u.name AS username, p.created_at AS createdAt, p.title AS title, user.avatar AS avatar FROM post p JOIN user u ON p.user_id = u.id " +
            "WHERE p.is_deleted = FALSE AND (:noFriend = TRUE OR p.user_id NOT IN :friendList)) " +
            "ORDER BY createdAt " +
            "LIMIT :pageSize OFFSET :page", nativeQuery = true)
    List<PostDto> allPostOrderByCreatedTime(Integer page, Integer pageSize, List<Long> friendList, Boolean noFriend);

    @Query("SELECT p.userId AS userId, p.id AS postId, u.name AS username, p.title AS title, p.createdAt AS createdAt, u.avatar AS avatar FROM Post p JOIN User u " +
            "ON p.userId = u.id WHERE p.isDeleted = FALSE")
    Optional<PostDto> getCommentWithUserByCid(Long id);
}