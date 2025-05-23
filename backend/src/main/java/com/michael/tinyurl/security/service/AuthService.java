package com.michael.tinyurl.security.service;

import com.michael.tinyurl.security.context.UserEntityContextHolder;
import com.michael.tinyurl.security.database.entity.LoginAttempt;
import com.michael.tinyurl.security.database.entity.UserEntity;
import com.michael.tinyurl.security.database.repos.UserRepository;
import com.michael.tinyurl.security.enums.LoginType;
import com.michael.tinyurl.security.exceptions.UnauthorizedException;
import com.michael.tinyurl.security.exceptions.UserLockedOutException;
import com.michael.tinyurl.security.jwt.JwtHelper;
import com.michael.tinyurl.security.models.JwtInformation;
import com.michael.tinyurl.security.models.LoginRequest;
import java.time.Instant;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class AuthService {
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtHelper jwtHelper;
    private final UserRepository userRepository;
    private final LoginAttemptService loginAttemptService;

    public AuthService(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder,
            JwtHelper jwtHelper,
            UserRepository userRepository,
            LoginAttemptService loginAttemptService) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.jwtHelper = jwtHelper;
        this.userRepository = userRepository;
        this.loginAttemptService = loginAttemptService;
    }

    @Transactional
    public JwtInformation login(LoginRequest loginRequest) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.username());
        UserEntity userEntity = UserEntityContextHolder.get();
        loginAttemptService.shouldLockUserOutOfAccount(userEntity);
        LoginAttempt attempt = buildInitialLoginAttempt(userEntity);
        boolean isSuccess = false;
        try {
            if (passwordEncoder.matches(loginRequest.password(), userDetails.getPassword())) {
                isSuccess = true;
                return jwtHelper.createToken(loginRequest.username());
            }
        } finally {
            attempt.setLoginType(isSuccess ? LoginType.SUCCESS : LoginType.FAILURE);
            loginAttemptService.saveLoginAttempt(attempt, userEntity);
        }
        throw new UserLockedOutException();
    }

    public JwtInformation register(LoginRequest loginRequest) {
        try {
            UserDetails details = userDetailsService.loadUserByUsername(loginRequest.username());
            if (details != null) {
                throw new UnauthorizedException();
            }
        } catch (UsernameNotFoundException ignored) {

        }

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(loginRequest.username());
        userEntity.setPassword(passwordEncoder.encode(loginRequest.password()));
        userRepository.save(userEntity);

        return jwtHelper.createToken(loginRequest.username());
    }

    private static LoginAttempt buildInitialLoginAttempt(UserEntity userEntity) {
        LoginAttempt attempt = new LoginAttempt();
        attempt.setUser(userEntity);
        attempt.setAttemptedAt(Instant.now());
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        attempt.setIpAddress(attributes.getRequest().getRemoteAddr());
        return attempt;
    }
}
