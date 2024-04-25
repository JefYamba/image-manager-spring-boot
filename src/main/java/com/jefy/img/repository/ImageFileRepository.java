package com.jefy.img.repository;

import com.jefy.img.entity.ImageFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @Author JefYamba
 * @Email joph.e.f.yamba@gmail.com
 * @Since 25/04/2024
 */
@Repository
public interface ImageFileRepository extends JpaRepository<ImageFile, Integer> {
    Optional<ImageFile> findByName(String fileName);
}
