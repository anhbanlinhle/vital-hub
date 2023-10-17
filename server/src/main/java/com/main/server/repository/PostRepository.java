package com.main.server.repository;

import com.main.server.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT p FROM Post p WHERE p.isDeleted = FALSE ORDER BY p.updatedAt, p.createdAt")
    Page<Post> allPostOrderByCreatedTime(Pageable pageable, List<Long> friendList, Boolean noFriend);
}

