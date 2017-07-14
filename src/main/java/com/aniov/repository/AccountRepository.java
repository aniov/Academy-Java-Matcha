package com.aniov.repository;

import com.aniov.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Account Repository
 */
public interface AccountRepository extends JpaRepository<Account, Long> {
}
