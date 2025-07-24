package com.portfolio.interview.controller;

import com.portfolio.interview.controller.docs.AuthControllerDocs;
import com.portfolio.interview.dto.AuthDto;
import com.portfolio.interview.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController implements AuthControllerDocs {
    private final AuthService authService;

    /**
     * 로그인
     */
    @PostMapping("/login")
    public AuthDto.Response login(@RequestBody AuthDto.LoginRequest loginRequest) {
        log.info("Login request received for user: {}", loginRequest.id());
        return authService.login(loginRequest);
    }

    /**
     * 리프레시 토큰
     */
    @PostMapping("/refresh")
    public AuthDto.Response refresh(@RequestBody AuthDto.RefreshRequest refreshRequest) {
        log.info("Refresh request received for refreshToken: {}", refreshRequest.refreshToken());
        return authService.refresh(refreshRequest);
    }

    /**
     * 로그아웃
     */
    @PostMapping("/logout")
    public AuthDto.LogoutResponse logout(@RequestBody AuthDto.LogoutRequest logoutRequest) {
        log.info("Logout request received for refreshToken {}", logoutRequest.refreshToken());
        return authService.logout(logoutRequest);
    }
}
