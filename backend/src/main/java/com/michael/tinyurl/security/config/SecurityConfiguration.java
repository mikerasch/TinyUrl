package com.michael.tinyurl.security.config;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import com.michael.tinyurl.security.filters.JwtSecurityFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration {
    private final JwtSecurityFilter jwtSecurityFilter;

    public SecurityConfiguration(JwtSecurityFilter jwtSecurityFilter) {
        this.jwtSecurityFilter = jwtSecurityFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        httpSecurity.authorizeHttpRequests(requests -> {
            requests.requestMatchers("/auth/**").permitAll();
            requests.requestMatchers("/**").authenticated();
        });

        httpSecurity.addFilterBefore(jwtSecurityFilter, UsernamePasswordAuthenticationFilter.class);

        httpSecurity.sessionManagement(management -> management.sessionCreationPolicy(STATELESS));

        return httpSecurity.build();
    }
}
