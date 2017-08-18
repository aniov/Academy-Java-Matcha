package com.aniov.repository;

import com.aniov.model.Interest;
import com.aniov.model.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Profile Repository
 */

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {

    Page<Profile> findAll(Pageable pageable);

    Page<Profile> findAllByInterestsEquals(Interest interest, Pageable pageable);

    Page<Profile> findByAddressIgnoreCaseContaining(String string, Pageable pageable);

}
