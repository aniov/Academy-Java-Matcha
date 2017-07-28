package com.aniov.repository;

import com.aniov.model.Profile;
import com.aniov.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Profile Repository
 */

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {

    Profile findByUserId(Long id);
}
