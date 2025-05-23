package com.michael.tinyurl.shared.database;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import java.util.Optional;

public class SpringSecurityAuditorAware implements AuditorAware<String> {
    private static final String ANONYMOUS_USER = "anonymousUser";
    @Override
    public Optional<String> getCurrentAuditor() {
        String username = Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getPrincipal)
                .map(User.class::cast)
                .map(User::getUsername)
                .orElse(ANONYMOUS_USER);

        return Optional.of(username);
    }
}
