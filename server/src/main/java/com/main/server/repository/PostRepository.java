package com.main.server.repository;

import com.main.server.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query(value = "(SELECT p.* FROM post p " +
            "WHERE p.is_deleted = FALSE AND (:noFriend = TRUE OR p.user_id IN :friendList)) " +
            "UNION " +
            "(SELECT p.* FROM post p " +
            "WHERE p.is_deleted = FALSE AND (:noFriend = TRUE OR p.user_id NOT IN :friendList)) " +
            "ORDER BY created_at " +
            "LIMIT :pageSize OFFSET :page", nativeQuery = true)
    List<Post> allPostOrderByCreatedTime(Integer page, Integer pageSize, List<Long> friendList, Boolean noFriend);

    Optional<Post> findByIdAndIsDeletedFalse(Long id);
}