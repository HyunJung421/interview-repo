package com.portfolio.interview.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.portfolio.interview.dto.AuthDto;
import com.portfolio.interview.dto.LoginDto;
import com.portfolio.interview.repository.RefreshTokenRepository;
import com.portfolio.interview.security.JwtUtil;
import com.portfolio.interview.service.UserService;
import com.portfolio.interview.system.enums.ResultCode;
import com.portfolio.interview.system.exception.RestApiException;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserService userService;

    @Value("${jwt.refresh.expiration}")
    private Long refreshTokenTimeOut;

    @PostMapping("/login")
    public AuthDto.Response login(@RequestBody LoginDto.Request loginRequest) {
        String id = loginRequest.id();
        String password = loginRequest.password();

        // 사용자 인증
        if (!userService.authenticate(id, password)) {
            throw new RestApiException(ResultCode.INVALID_AUTH_INFO);
        }

        // 인증 성공 시 토큰 생성
        String accessToken = jwtUtil.generateAccessToken(id);
        String refreshToken = jwtUtil.generateRefreshToken(id);
        refreshTokenRepository.saveRefreshToken(id, refreshToken, refreshTokenTimeOut);

        return new AuthDto.Response(accessToken, refreshToken);
    }

    @PostMapping("/refresh")
    public AuthDto.Response refresh(@RequestBody LoginDto.Request loginRequest) {
        String refreshToken = loginRequest.refreshToken();

        String id = jwtUtil.extractId(refreshToken);
        String storedRefreshToken = refreshTokenRepository.getRefreshToken(id);

        // redis에 저장된 refreshToken과 일치하는지 확인
        if (storedRefreshToken != null
                && storedRefreshToken.equals(refreshToken)
                && jwtUtil.validateToken(refreshToken)) {
            String newAccessToken = jwtUtil.generateAccessToken(loginRequest.id());
            String newRefreshToken = jwtUtil.generateRefreshToken(loginRequest.id());

            // 기존 refreshToken 삭제
            refreshTokenRepository.deleteRefreshToken(id);

            // 새로운 refreshToken 저장
            refreshTokenRepository.saveRefreshToken(id, newRefreshToken, refreshTokenTimeOut);

            return new AuthDto.Response(newAccessToken, newRefreshToken);
        }

        throw new RestApiException(ResultCode.INVALID_REFRESH_TOKEN);
    }

    @PostMapping("/logout")
    public Boolean logout(@RequestBody LoginDto.Request loginRequest) {
        String id = loginRequest.id();
        String refreshToken = loginRequest.refreshToken();

        // Redis에서 Refresh Token 삭제
        String storedRefreshToken = refreshTokenRepository.getRefreshToken(id);

        if (storedRefreshToken != null && storedRefreshToken.equals(refreshToken)) {
            refreshTokenRepository.deleteRefreshToken(id);
            return true;
        }

        throw new RestApiException(ResultCode.LOGOUT_FAILED);
    }
}
