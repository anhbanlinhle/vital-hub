package com.main.server.repository;

import com.main.server.entity.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FriendRepository extends JpaRepository<Friend, Long> {

    @Query(value = "(SELECT f1.first_user_id AS friend FROM friend f1 WHERE f1.second_user_id = :userId AND f1.status = 'ACCEPTED') " +
            "UNION " +
            "(SELECT f1.second_user_id AS friend FROM friend f1 WHERE f1.first_user_id = :userId AND f1.status = 'ACCEPTED')", nativeQuery = true)
    List<Long> findFriendList(Long userId);

}
