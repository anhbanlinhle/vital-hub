package com.main.server.repository;

import com.main.server.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {


    @Query("SELECT u.id FROM User u WHERE u.gmail = :gmail")
    Long idByGmail(String gmail);
    Optional<User> findByGmail(String gmail);

    @Query("SELECT u.id FROM User u WHERE u.gmail = ?1")
    Long findIdByGmail(String gmail);
}
