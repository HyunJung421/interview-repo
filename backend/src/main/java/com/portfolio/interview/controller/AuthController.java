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

    /**
     * 로그인
     *
     * @param loginRequest
     * @return
     */
    @PostMapping("/login")
    public AuthDto.Response login(@RequestBody LoginDto.Request loginRequest) {
        log.info("Login request received for user: {}", loginRequest.id());
        return authService.login(loginRequest);
    }

    /**
     * 리프레시 토큰
     *
     * @param loginRequest
     * @return
     */
    @PostMapping("/refresh")
    public AuthDto.Response refresh(@RequestBody LoginDto.Request loginRequest) {
        log.info("Refresh request received for user: {}", loginRequest.id());
        return authService.refresh(loginRequest);
    }

    /**
     * 로그아웃
     *
     * @param loginRequest
     * @return
     */
    @PostMapping("/logout")
    public LogoutDto.Response logout(@RequestBody LoginDto.Request loginRequest) {
        log.info("Logout request received for refreshToken {}", loginRequest.refreshToken());
        return authService.logout(loginRequest);
    }
}
