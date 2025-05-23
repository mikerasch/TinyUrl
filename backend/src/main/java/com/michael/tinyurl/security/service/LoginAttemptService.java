package com.michael.tinyurl.security.service;

import com.michael.tinyurl.security.database.entity.LoginAttempt;
import com.michael.tinyurl.security.database.entity.UserEntity;
import com.michael.tinyurl.security.database.repos.LoginAttemptRepository;
import com.michael.tinyurl.security.database.repos.UserRepository;
import com.michael.tinyurl.security.enums.LoginType;
import com.michael.tinyurl.security.exceptions.UserLockedOutException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LoginAttemptService {
    private final UserRepository userRepository;
    private final LoginAttemptRepository loginAttemptRepository;

    public LoginAttemptService(UserRepository userRepository, LoginAttemptRepository loginAttemptRepository) {
        this.userRepository = userRepository;
        this.loginAttemptRepository = loginAttemptRepository;
    }

    public void shouldLockUserOutOfAccount(UserEntity userEntity) {
        Instant instantToFilterLatestAttempts = Instant.now().minus(15, ChronoUnit.MINUTES);
        List<LoginAttempt> attempts = userRepository.fetchLastThreeAttempts(userEntity, instantToFilterLatestAttempts);
        if (attempts.size() >= 3
                && attempts.stream().allMatch(attempt -> attempt.getLoginType() == LoginType.FAILURE)) {
            throw new UserLockedOutException();
        }
    }

    @Transactional
    public void saveLoginAttempt(LoginAttempt attempt, UserEntity userEntity) {
        userEntity.addLoginAttempt(attempt);
        loginAttemptRepository.saveAndFlush(attempt);
        userRepository.saveAndFlush(userEntity);
    }
}
