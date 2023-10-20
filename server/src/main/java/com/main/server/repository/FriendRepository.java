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


<<<<<<< HEAD
    @Query(value = "(SELECT f1.first_user_id AS friend FROM friend f1 WHERE f1.second_user_id = :userId AND f1.status = 'ACCEPTED') " +
            "UNION " +
            "(SELECT f1.second_user_id AS friend FROM friend f1 WHERE f1.first_user_id = :userId AND f1.status = 'ACCEPTED')", nativeQuery = true)
    List<Long> findFriendList(Long userId);

=======
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
                                AND IF(:name IS NOT NULL, name LIKE CONCAT('%', :name, '%'), 1)
                                ORDER BY name
                                LIMIT :limit OFFSET :offset
                    """
    )
    public List<FriendListDto> getFriendList(Long id,String name, Integer limit, Integer offset);
>>>>>>> main
}
