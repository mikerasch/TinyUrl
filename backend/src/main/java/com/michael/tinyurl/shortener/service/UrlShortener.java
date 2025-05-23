package com.michael.tinyurl.shortener.service;

import com.michael.tinyurl.security.context.UserEntityContextHolder;
import com.michael.tinyurl.security.database.entity.UserEntity;
import com.michael.tinyurl.security.database.repos.UserRepository;
import com.michael.tinyurl.shortener.database.entities.ShortenedUrlEntity;
import com.michael.tinyurl.shortener.database.repos.ShortenedUrlRepository;
import com.michael.tinyurl.shortener.models.ShortenedUrl;
import jakarta.persistence.EntityManager;
import org.springframework.transaction.annotation.Transactional;

public abstract sealed class UrlShortener permits MinimizeUrlShortener, UniqueUrlShortener {
    private final ShortenedUrlRepository shortenedUrlRepository;
    private final EntityManager entityManager;
    private final UserRepository userRepository;

    protected UrlShortener(
            ShortenedUrlRepository shortenedUrlRepository, EntityManager entityManager, UserRepository userRepository) {
        this.shortenedUrlRepository = shortenedUrlRepository;
        this.entityManager = entityManager;
        this.userRepository = userRepository;
    }

    abstract ShortenedUrlEntity applyStrategy(ShortenedUrlEntity entity);

    @Transactional
    public ShortenedUrl shorten(String url) {
        ShortenedUrlEntity shortenedUrlEntity = new ShortenedUrlEntity();
        shortenedUrlEntity.setOriginalUrl(url);
        UserEntity userEntity = UserEntityContextHolder.get(entityManager);
        if (userEntity != null) {
            shortenedUrlEntity.setUser(userEntity);
            userEntity.getShortenedUrl().add(shortenedUrlEntity);
        }

        shortenedUrlEntity = applyStrategy(shortenedUrlEntity);

        shortenedUrlRepository.save(shortenedUrlEntity);
        if (userEntity != null) {
            userRepository.save(userEntity);
        }
        return new ShortenedUrl(url, shortenedUrlEntity.getId());
    }
}
