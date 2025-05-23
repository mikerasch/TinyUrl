package com.michael.tinyurl.shortener.database.repos;

import com.michael.tinyurl.security.database.entity.UserEntity;
import com.michael.tinyurl.shortener.database.entities.ShortenedUrlEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShortenedUrlRepository extends JpaRepository<ShortenedUrlEntity, String> {
    Page<ShortenedUrlEntity> findAllByUser(UserEntity userEntity, Pageable pageable);
}
