package com.aniov.repository;

import com.aniov.model.Interest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * Interest repository
 */
@Repository
public interface InterestRepository extends JpaRepository<Interest, Long>{

    Interest findByInterest(String interest);
}
