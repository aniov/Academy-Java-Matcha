package com.aniov.repository;

import com.aniov.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * User Repository
 */

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);
    User findByEmail(String email);
}
