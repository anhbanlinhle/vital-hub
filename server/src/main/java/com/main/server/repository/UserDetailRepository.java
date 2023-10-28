package com.main.server.repository;

import com.main.server.entity.UserDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserDetailRepository extends JpaRepository<UserDetail, Long> {

    UserDetail findByUserId(Long id);

    @Query(
            nativeQuery = true,
            value = "SELECT id FROM user_detail WHERE user_id = ?1"
    )
    Long findIdByUserId(Long selfId);
}
