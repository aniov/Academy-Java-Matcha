package com.aniov.repository;

import com.aniov.model.User;
import com.aniov.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * VerificationToken Repository
 */

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

    VerificationToken findByToken(String token);

    List<VerificationToken> findAllByUser(User user);

}
