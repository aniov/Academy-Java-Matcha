package com.aniov.repository;

import com.aniov.model.Message;
import com.aniov.model.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * A message Repository interface
 */
@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    Page<Message> findBySentFromProfileOrSentToProfile(Profile sentFromProfile, Profile sentToProfile, Pageable pageable);
}
