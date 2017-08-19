package com.aniov.repository;

import com.aniov.model.Visitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Visitor repository
 */
@Repository
public interface VisitorRepository extends JpaRepository<Visitor, Long> {

    List<Visitor> findDistinctByProfileUserUsernameOrderByVisitDateDesc(String profile_user_username);
}
