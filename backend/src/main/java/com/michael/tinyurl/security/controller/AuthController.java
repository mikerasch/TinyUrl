package com.michael.tinyurl.security.controller;

import com.michael.tinyurl.security.models.JwtInformation;
import com.michael.tinyurl.security.models.LoginRequest;
import com.michael.tinyurl.security.service.AuthService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@CrossOrigin(value = "*")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public JwtInformation login(@RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @PostMapping("/register")
    public JwtInformation register(@RequestBody LoginRequest loginRequest) {
        return authService.register(loginRequest);
    }
}
