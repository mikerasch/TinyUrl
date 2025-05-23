package com.michael.tinyurl.security.service;

import com.michael.tinyurl.security.context.UserEntityContextHolder;
import com.michael.tinyurl.security.database.entity.UserEntity;
import com.michael.tinyurl.security.database.repos.UserRepository;
import java.util.Collections;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    public UserDetailServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository
                .findByUsername(username)
                .map(userEntity -> {
                    UserEntityContextHolder.set(userEntity);
                    return convertToUserDetails(userEntity);
                })
                .orElseThrow(() -> new UsernameNotFoundException("User not found."));
    }

    private UserDetails convertToUserDetails(UserEntity user) {
        return new User(user.getUsername(), user.getPassword(), Collections.emptyList());
    }
}
