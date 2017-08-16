package com.aniov.repository;

import com.aniov.model.Interest;
import com.aniov.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Profile Repository
 */

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {

    Profile findByUserId(Long id);

    List<Profile> findAllByInterestsEquals(Interest interest);
}
