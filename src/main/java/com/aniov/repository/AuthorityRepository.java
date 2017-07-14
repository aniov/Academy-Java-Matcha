package com.aniov.repository;

import com.aniov.model.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Authority Repository
 */
public interface AuthorityRepository extends JpaRepository<Authority, Long> {
}
