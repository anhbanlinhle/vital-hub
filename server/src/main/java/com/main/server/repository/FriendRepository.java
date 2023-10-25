package com.main.server.repository;

import com.main.server.entity.Friend;
import com.main.server.utils.dto.UserDto;
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
                                AND IF(:name IS NOT NULL, name LIKE CONCAT('%', :name, '%'), 1)
                                ORDER BY name
                                LIMIT :limit OFFSET :offset
                    """
    )
    public List<UserDto> getFriendList(Long id, String name, Integer limit, Integer offset);

    @Query(
            nativeQuery = true,
            value = """
                                INSERT INTO friend (first_user_id, second_user_id, status)
                                VALUES (:firstUserId, :secondUserId, 'PENDING')
                    """
    )
    void addFriend(Long firstUserId, Long secondUserId);

    @Query(
            nativeQuery = true,
            value = """
                                DELETE FROM friend
                                WHERE (first_user_id = :firstUserId AND second_user_id = :secondUserId)
                                OR (first_user_id = :secondUserId AND second_user_id = :firstUserId)
                    """
    )
    void deleteFriend(Long firstUserId, Long secondUserId);

    @Query(
            nativeQuery = true,
            value = """
                                UPDATE friend
                                SET status = 'ACCEPTED'
                                WHERE (first_user_id = :firstUserId AND second_user_id = :secondUserId)
                                OR (first_user_id = :secondUserId AND second_user_id = :firstUserId)
                    """
    )
    void acceptFriend(Long firstUserId, Long secondUserId);

    @Query(
            nativeQuery = true,
            value = """
                                DELETE FROM friend
                                WHERE (first_user_id = :firstUserId AND second_user_id = :secondUserId)
                    """
    )
    void denyFriend(Long firstUserId, Long secondUserId);
}
