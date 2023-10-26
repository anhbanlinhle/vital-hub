package com.main.server.repository;

import com.main.server.entity.User;
import com.main.server.utils.dto.UserDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {


    @Query("SELECT u.id FROM User u WHERE u.gmail = :gmail")
    Long idByGmail(String gmail);

    Optional<User> findByGmail(String gmail);

    @Query("SELECT u.id FROM User u WHERE u.gmail = ?1")
    Long findIdByGmail(String gmail);

    @Query(
            nativeQuery = true,
            value = """
                    SELECT u.id, u.name, u.avatar,
                    CASE WHEN :self_id = :other_id THEN 'SELF'
                    WHEN EXISTS (
                        SELECT * FROM friend
                        WHERE ((first_user_id = :self_id AND second_user_id = :other_id)                        
                        OR (first_user_id = :other_id AND second_user_id = :self_id))
                        AND friend.status = 'ACCEPTED'
                    ) THEN 'FRIEND'
                    WHEN EXISTS (
                        SELECT * FROM friend
                        WHERE first_user_id = :self_id AND second_user_id = :other_id
                        AND friend.status = 'PENDING'
                    ) THEN 'PENDING'
                    WHEN EXISTS (
                        SELECT * FROM friend
                        WHERE first_user_id = :other_id AND second_user_id = :self_id
                        AND friend.status = 'PENDING'
                    ) THEN 'INCOMING'
                    ELSE 'NONE' END AS status
                    FROM user u
                    WHERE u.id = :other_id
                    """
    )
    UserDto findUserDtoById(Long self_id, Long other_id);

    @Query(
            nativeQuery = true,
            value = """
                    SELECT u.id, u.name, u.avatar,
                    CASE
                            WHEN EXISTS (
                                SELECT * FROM friend
                                WHERE ((first_user_id = :self_id AND second_user_id = u.id)                              
                                OR (first_user_id = u.id AND second_user_id = :self_id))
                                AND friend.status = 'ACCEPTED'
                            ) THEN 'FRIEND'
                            WHEN EXISTS (
                                SELECT * FROM friend
                                WHERE first_user_id = :self_id AND second_user_id = u.id
                                AND friend.status = 'PENDING'
                            ) THEN 'PENDING'
                            WHEN EXISTS (
                                SELECT * FROM friend 
                                WHERE first_user_id = u.id AND second_user_id = :self_id
                                AND status = 'PENDING'
                            ) THEN 'INCOMING'
                            ELSE 'NONE'
                        END AS status
                    FROM user u
                    WHERE u.id != :self_id
                    AND (u.name LIKE CONCAT('%', :name, '%')
                        OR u.gmail LIKE CONCAT('%', :name, '%'))
                    ORDER BY FIELD(status, 'FRIEND', 'PENDING', 'INCOMING', 'NONE'), u.name
                    LIMIT :limit OFFSET :offset
                    """
    )
    List<UserDto> findUser(Long self_id, String name, Integer limit, Integer offset);
}
