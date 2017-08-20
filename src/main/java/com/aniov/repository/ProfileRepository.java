package com.aniov.repository;

import com.aniov.model.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * Profile Repository
 */

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {

    Page<Profile> findAll(Pageable pageable);

    Page<Profile> findDistinctProfilesByUserAccountEnabledIsTrueAndInterestsInterestIgnoreCaseContainingAndIdNot(String interest, Long id, Pageable pageable);

    Page<Profile> findDistinctProfilesByUserAccountEnabledIsTrueAndAddressIgnoreCaseContainingAndIdNot(String string, Long id, Pageable pageable);

    Page<Profile> findDistinctProfilesByUserAccountEnabledIsTrueAndUserUsernameIgnoreCaseContainingAndIdNot(String username, Long id, Pageable pageable);

    Page<Profile> findDistinctProfilesByUserAccountEnabledIsTrueAndIdNot(Long id, Pageable pageable);

    Page<Profile> findDistinctProfilesByUserAccountEnabledIsTrueAndIdNotAndLookingForAndGenderOrLookingForAndGenderAndIdNot(
            Long id, Set<Profile.Gender> lookingFor, Profile.Gender gender, Set<Profile.Gender> lookingFor2, Profile.Gender gender2, Long id2, Pageable pageable);

    Page<Profile> findDistinctByIdNot(Long id, Pageable pageable);

}
