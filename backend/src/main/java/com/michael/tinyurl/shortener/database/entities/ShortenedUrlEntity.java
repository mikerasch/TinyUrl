package com.michael.tinyurl.shortener.database.entities;

import com.michael.tinyurl.security.database.entity.UserEntity;
import com.michael.tinyurl.shared.database.AuditInfoEntity;
import jakarta.persistence.*;

@Entity
public class ShortenedUrlEntity {
    @Id
    private String id;

    @Lob
    private String originalUrl;

    @ManyToOne
    private UserEntity user;

    @Embedded
    private AuditInfoEntity auditInfo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public AuditInfoEntity getAuditInfo() {
        return auditInfo;
    }

    public void setAuditInfo(AuditInfoEntity auditInfo) {
        this.auditInfo = auditInfo;
    }
}
