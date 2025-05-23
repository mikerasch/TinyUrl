package com.michael.tinyurl.shortener.service;

import com.michael.tinyurl.shortener.database.entities.ShortenedUrlEntity;
import com.michael.tinyurl.shortener.database.repos.ShortenedUrlRepository;
import com.michael.tinyurl.shortener.exceptions.InvalidIdException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class UrlRedirectService {
    private final ShortenedUrlRepository shortenedUrlRepository;

    public UrlRedirectService(ShortenedUrlRepository shortenedUrlRepository) {
        this.shortenedUrlRepository = shortenedUrlRepository;
    }

    @Cacheable(value = "shortenedUrls", key = "#id", unless = "#result == null")
    public String redirect(String id) {
        return shortenedUrlRepository
                .findById(id)
                .map(ShortenedUrlEntity::getOriginalUrl)
                .orElseThrow(() -> new InvalidIdException("Invalid id: " + id));
    }
}
