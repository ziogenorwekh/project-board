package com.example.projectboard.repository;


import com.example.projectboard.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByEmail(String email);

    @Query("select u from User u where u.userId = ?1")
    Optional<User> findUserByUserId(String userId);
}
