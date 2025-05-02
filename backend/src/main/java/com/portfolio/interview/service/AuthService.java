package com.portfolio.interview.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.portfolio.interview.dto.AuthDto;
import com.portfolio.interview.dto.LoginDto;
import com.portfolio.interview.dto.LogoutDto;
import com.portfolio.interview.repository.RefreshTokenRepository;
import com.portfolio.interview.security.JwtUtil;
import com.portfolio.interview.system.enums.ResultCode;
import com.portfolio.interview.system.exception.RestApiException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserService userService;

    @Value("${jwt.refresh.expiration}")
    private Long refreshTokenTimeOut;

    // 로그인 처리
    public AuthDto.Response login(LoginDto.Request loginRequest) {
        String id = loginRequest.id();
        String password = loginRequest.password();

        // 사용자 인증
        authenticateUser(id, password);

        // 인증 성공 시 토큰 생성
        String accessToken = createAccessToken(id);
        String refreshToken = createRefreshToken(id);

        saveRefreshToken(id, refreshToken);

        return new AuthDto.Response(accessToken, refreshToken);
    }

    // 리프레시 토큰 처리
    public AuthDto.Response refresh(LoginDto.Request loginRequest) {
        String refreshToken = loginRequest.refreshToken();

        // Refresh Token 유효성 검사
        String id = validateRefreshToken(refreshToken);

        // 새로운 Access Token 및 Refresh Token 생성
        String newAccessToken = createAccessToken(id);
        String newRefreshToken = createRefreshToken(id);

        // 기존 Refresh Token 삭제 및 새로 저장
        updateRefreshToken(id, newRefreshToken);

        return new AuthDto.Response(newAccessToken, newRefreshToken);
    }

    // 로그아웃 처리
    public LogoutDto.Response logout(LoginDto.Request loginRequest) {
        String id = loginRequest.id();
        String refreshToken = loginRequest.refreshToken();

        // Redis에서 Refresh Token 삭제
        String storedRefreshToken = refreshTokenRepository.getRefreshToken(id);

        // 실패 조건 처리
        if (storedRefreshToken == null || !storedRefreshToken.equals(refreshToken)) {
            throw new RestApiException(ResultCode.LOGOUT_FAILED);
        }

        // 성공 조건 처리
        refreshTokenRepository.deleteRefreshToken(id);
        return new LogoutDto.Response(true, "Logout successfully");
    }

    // 사용자 인증
    private void authenticateUser(String id, String password) {
        if (!userService.authenticate(id, password)) {
            throw new RestApiException(ResultCode.INVALID_AUTH_INFO);
        }
    }

    // Refresh Token 저장
    private void saveRefreshToken(String id, String refreshToken) {
        refreshTokenRepository.saveRefreshToken(id, refreshToken, refreshTokenTimeOut);
    }

    private String validateRefreshToken(String refreshToken) {
        String id = jwtUtil.extractId(refreshToken);
        String storedRefreshToken = refreshTokenRepository.getRefreshToken(id);

        if (storedRefreshToken == null
                || !storedRefreshToken.equals(refreshToken)
                || !jwtUtil.validateToken(refreshToken)) {
            throw new RestApiException(ResultCode.INVALID_REFRESH_TOKEN);
        }

        return id;
    }

    private void updateRefreshToken(String id, String newRefreshToken) {
        refreshTokenRepository.deleteRefreshToken(id);
        saveRefreshToken(id, newRefreshToken);
    }

    private String createAccessToken(String id) {
        return jwtUtil.generateAccessToken(id);
    }

    private String createRefreshToken(String id) {
        return jwtUtil.generateRefreshToken(id);
    }
}
