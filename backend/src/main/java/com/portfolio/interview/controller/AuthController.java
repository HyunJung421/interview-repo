package com.portfolio.interview.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.portfolio.interview.dto.AuthDto;
import com.portfolio.interview.dto.LoginDto;
import com.portfolio.interview.dto.LogoutDto;
import com.portfolio.interview.service.AuthService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final AuthService authService;

    @Value("${jwt.refresh.expiration}")
    private Long refreshTokenTimeOut;

    @PostMapping("/login")
    public AuthDto.Response login(@RequestBody LoginDto.Request loginRequest) {
        log.info("Login request received for user: {}", loginRequest.id());
        return authService.login(loginRequest);
    }

    @PostMapping("/refresh")
    public AuthDto.Response refresh(@RequestBody LoginDto.Request loginRequest) {
        log.info("Refresh request received for user: {}", loginRequest.id());
        return authService.refresh(loginRequest);
    }

    @PostMapping("/logout")
    public LogoutDto.Response logout(@RequestBody LoginDto.Request loginRequest) {
        log.info("Logout request received for user: {}", loginRequest.id());
        return authService.logout(loginRequest);
    }
}
