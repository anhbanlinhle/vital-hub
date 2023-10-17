package com.main.server.repository;

import com.main.server.entity.Friend;
import com.main.server.entity.User;
import com.main.server.utils.dto.FriendListDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FriendRepository extends JpaRepository<Friend, Long> {
    @Query(
            nativeQuery = true,
            value = """
                                SELECT COUNT(*) FROM friend
                                WHERE first_user_id = :id OR second_user_id = :id
                    """
    )
    public int countFriend(Long id);


    @Query(
            nativeQuery = true,
            value = """
                                SELECT id, name, avatar FROM user
                                WHERE id IN (
                                    SELECT first_user_id FROM friend
                                    WHERE second_user_id = :id
                                    AND friend.status = 'ACCEPTED'
                                    UNION
                                    SELECT second_user_id FROM friend
                                    WHERE first_user_id = :id
                                    AND friend.status = 'ACCEPTED'
                                )
                                
                                ORDER BY name
                                LIMIT :limit OFFSET :offset
                    """
    )
    public List<FriendListDto> getFriendList(Long id, Integer limit, Integer offset);
}
