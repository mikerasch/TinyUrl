package com.michael.tinyurl.security.database.entity;

import com.michael.tinyurl.shortener.database.entities.ShortenedUrlEntity;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String username;

    @Column
    private String password;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
    private List<ShortenedUrlEntity> shortenedUrl;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
    private List<LoginAttempt> loginAttempts;

    public List<LoginAttempt> getLoginAttempts() {
        return loginAttempts;
    }

    public void addLoginAttempt(LoginAttempt attempt) {
        if (loginAttempts == null) {
            loginAttempts = new ArrayList<>();
        }
        loginAttempts.add(attempt);
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<ShortenedUrlEntity> getShortenedUrl() {
        if (shortenedUrl == null) {
            return new ArrayList<>();
        }
        return shortenedUrl;
    }

    public void setShortenedUrl(List<ShortenedUrlEntity> shortenedUrl) {
        this.shortenedUrl = shortenedUrl;
    }
}
