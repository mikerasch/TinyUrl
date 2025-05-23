package com.michael.tinyurl.security.filters;

import com.michael.tinyurl.security.context.UserEntityContextHolder;
import com.michael.tinyurl.security.enums.UserType;
import com.michael.tinyurl.security.exceptions.UnauthorizedException;
import com.michael.tinyurl.security.jwt.JwtHelper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtSecurityFilter extends OncePerRequestFilter {
    private final JwtHelper jwtHelper;
    private final UserDetailsService userDetailsService;

    public JwtSecurityFilter(JwtHelper helper, UserDetailsService userDetailsService) {
        this.jwtHelper = helper;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            if (request.getRequestURI().startsWith("/auth")) {
                return;
            }

            String header = request.getHeader("Authorization");
            if (header == null || !header.startsWith("Bearer ")) {
                setAnonymousUser();
                return;
            }

            String token = header.substring(7);
            String username = jwtHelper.extractUsername(token).orElse(null);

            if (username == null) {
                throw new UnauthorizedException();
            }

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            setLoggedInUser(userDetails);

        } finally {
            filterChain.doFilter(request, response);
            UserEntityContextHolder.remove();
        }
    }

    private void setLoggedInUser(UserDetails userDetails) {
        Authentication auth = new UsernamePasswordAuthenticationToken(
                userDetails, null, List.of(UserType.LOGGED_IN.getGrantedAuthority()));
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    private void setAnonymousUser() {
        Authentication anonymousAuth = new UsernamePasswordAuthenticationToken(
                "anonymousUser", null, List.of(UserType.ANONYMOUS.getGrantedAuthority()));
        SecurityContextHolder.getContext().setAuthentication(anonymousAuth);
    }
}
