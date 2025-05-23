package com.michael.tinyurl.shortener.controller;

import com.michael.tinyurl.shortener.service.UrlRedirectService;
import java.net.URI;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/r/")
@CrossOrigin(value = "*")
public class UrlRedirectController {
    private final UrlRedirectService urlRedirectService;

    public UrlRedirectController(UrlRedirectService urlRedirectService) {
        this.urlRedirectService = urlRedirectService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Void> redirect(@PathVariable String id) {
        String redirectUrl = urlRedirectService.redirect(id);
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(redirectUrl))
                .build();
    }
}
