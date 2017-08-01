package com.aniov.repository;

import com.aniov.model.Picture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Picture repository
 */
@Repository
public interface PictureRepository extends JpaRepository<Picture, Long>{


}
