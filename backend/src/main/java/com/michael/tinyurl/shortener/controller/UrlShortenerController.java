package com.michael.tinyurl.shortener.controller;

import com.michael.tinyurl.shortener.enums.Strategy;
import com.michael.tinyurl.shortener.models.ShortenedUrl;
import com.michael.tinyurl.shortener.models.StrategyResponse;
import com.michael.tinyurl.shortener.service.UrlShortenerService;
import com.michael.tinyurl.shortener.validators.UrlValidator;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/shortener")
@CrossOrigin(value = "*")
public class UrlShortenerController {
    private final UrlShortenerService urlShortenerService;

    public UrlShortenerController(UrlShortenerService urlShortenerService) {
        this.urlShortenerService = urlShortenerService;
    }

    @PostMapping("/minimize")
    public ShortenedUrl minimizeUrl(
            @RequestParam String url, @RequestParam(required = false, defaultValue = "MINIMIZE") Strategy strategy) {
        UrlValidator.validateUrl(url);
        return urlShortenerService.shortenUrl(url, strategy);
    }

    @GetMapping("/strategies")
    public List<StrategyResponse> getStrategies() {
        return urlShortenerService.getStrategies();
    }

    @GetMapping("/history")
    public Page<ShortenedUrl> getHistory(Pageable pageable) {
        return urlShortenerService.getHistory(pageable);
    }

    @GetMapping("/search-by-id")
    public ShortenedUrl searchByUniqueId(@RequestParam String id) {
        return urlShortenerService.searchByUniqueId(id);
    }
}
