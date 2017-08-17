package com.aniov.repository;

import com.aniov.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * User Repository
 */

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);
    User findByEmail(String email);
    User findByUsernameIgnoreCase(String username);
    List<User> findByUsernameIgnoreCaseContaining(String string);
}
