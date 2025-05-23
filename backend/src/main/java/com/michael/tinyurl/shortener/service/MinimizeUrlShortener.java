package com.michael.tinyurl.shortener.service;

import com.michael.tinyurl.security.database.repos.UserRepository;
import com.michael.tinyurl.shortener.database.entities.ShortenedUrlEntity;
import com.michael.tinyurl.shortener.database.repos.ShortenedUrlRepository;
import com.michael.tinyurl.shortener.generator.IdGenerator;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;

@Service
public non-sealed class MinimizeUrlShortener extends UrlShortener {

    public MinimizeUrlShortener(
            ShortenedUrlRepository shortenedUrlRepository, EntityManager entityManager, UserRepository userRepository) {
        super(shortenedUrlRepository, entityManager, userRepository);
    }

    @Override
    public ShortenedUrlEntity applyStrategy(ShortenedUrlEntity shortenedUrlEntity) {
        String id = IdGenerator.generateId();
        shortenedUrlEntity.setId(id);
        return shortenedUrlEntity;
    }
}
