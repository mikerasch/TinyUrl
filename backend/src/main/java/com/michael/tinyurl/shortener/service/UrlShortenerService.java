package com.michael.tinyurl.shortener.service;

import com.michael.tinyurl.security.context.UserEntityContextHolder;
import com.michael.tinyurl.security.database.entity.UserEntity;
import com.michael.tinyurl.shortener.database.entities.ShortenedUrlEntity;
import com.michael.tinyurl.shortener.database.repos.ShortenedUrlRepository;
import com.michael.tinyurl.shortener.enums.Strategy;
import com.michael.tinyurl.shortener.models.ShortenedUrl;
import com.michael.tinyurl.shortener.models.StrategyResponse;
import jakarta.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UrlShortenerService {
    private final UrlShortener minimizeUrlShortener;
    private final UrlShortener uniqueUrlShortener;
    private final EntityManager entityManager;
    private final ShortenedUrlRepository shortenedUrlRepository;

    public UrlShortenerService(
            UrlShortener minimizeUrlShortener,
            UrlShortener uniqueUrlShortener,
            EntityManager entityManager,
            ShortenedUrlRepository shortenedUrlRepository) {
        this.minimizeUrlShortener = minimizeUrlShortener;
        this.uniqueUrlShortener = uniqueUrlShortener;
        this.entityManager = entityManager;
        this.shortenedUrlRepository = shortenedUrlRepository;
    }

    @Transactional
    public ShortenedUrl shortenUrl(String url, Strategy strategy) {
        switch (strategy) {
            case UNIQUE -> {
                return uniqueUrlShortener.shorten(url);
            }
            case MINIMIZE -> {
                return minimizeUrlShortener.shorten(url);
            }
            default -> throw new IllegalArgumentException("Unknown strategy: " + strategy);
        }
    }

    public List<StrategyResponse> getStrategies() {
        return Arrays.stream(Strategy.values())
                .map(strategy -> new StrategyResponse(strategy.name(), strategy.isDefault()))
                .toList();
    }

    @Transactional
    public Page<ShortenedUrl> getHistory(Pageable pageable) {
        UserEntity userEntity = UserEntityContextHolder.get(entityManager);

        if (userEntity == null) {
            return Page.empty();
        }

        Page<ShortenedUrlEntity> shortenedUrlPage = shortenedUrlRepository.findAllByUser(userEntity, pageable);

        return shortenedUrlPage.map(shortenedUrlEntity ->
                new ShortenedUrl(shortenedUrlEntity.getOriginalUrl(), shortenedUrlEntity.getId()));
    }

    public ShortenedUrl searchByUniqueId(String id) {
        ShortenedUrlEntity shortenedUrlEntity =
                shortenedUrlRepository.findById(id).orElse(null);
        return Optional.ofNullable(shortenedUrlEntity)
                .map(shortenedUrl -> new ShortenedUrl(shortenedUrl.getId(), shortenedUrl.getOriginalUrl()))
                .orElse(null);
    }
}
