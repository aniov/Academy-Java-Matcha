package com.aniov.repository;

import com.aniov.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Marius on 7/12/2017.
 */
public interface UserRepository extends JpaRepository<User, Long> {
}
