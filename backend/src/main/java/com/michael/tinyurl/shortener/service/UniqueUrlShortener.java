package com.michael.tinyurl.shortener.service;

import com.michael.tinyurl.security.database.repos.UserRepository;
import com.michael.tinyurl.shortener.database.entities.ShortenedUrlEntity;
import com.michael.tinyurl.shortener.database.repos.ShortenedUrlRepository;
import jakarta.persistence.EntityManager;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public non-sealed class UniqueUrlShortener extends UrlShortener {

    public UniqueUrlShortener(
            ShortenedUrlRepository shortenedUrlRepository, EntityManager entityManager, UserRepository userRepository) {
        super(shortenedUrlRepository, entityManager, userRepository);
    }

    @Override
    public ShortenedUrlEntity applyStrategy(ShortenedUrlEntity entity) {
        String id = UUID.randomUUID().toString();
        entity.setId(id);
        return entity;
    }
}
