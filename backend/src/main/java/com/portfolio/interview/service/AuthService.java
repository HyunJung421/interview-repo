package com.portfolio.interview.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.portfolio.interview.dto.AuthDto;
import com.portfolio.interview.dto.LoginDto;
import com.portfolio.interview.dto.LogoutDto;
import com.portfolio.interview.repository.RefreshTokenRepository;
import com.portfolio.interview.security.JwtUtil;
import com.portfolio.interview.system.enums.ResultCode;
import com.portfolio.interview.system.exception.RestApiException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.refresh.expiration}")
    private Long refreshTokenTimeOut;

    // 로그인 처리
    public AuthDto.Response login(LoginDto.Request loginRequest) {
        String id = loginRequest.id();
        String password = loginRequest.password();

        // 사용자 정보 조회
        UserDetails userDetails = userDetailsService.loadUserByUsername(id);

        if (userDetails == null) {
            throw new RestApiException(ResultCode.USER_NOT_FOUND);
        }

        // 비밀번호 확인
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new RestApiException(ResultCode.INVALID_PASSWORD);
        }

        // 인증 성공 시 토큰 생성
        AuthDto.Tokens tokens = generateTokens(id);

        saveRefreshToken(id, tokens.refreshToken());

        return new AuthDto.Response(tokens.accessToken(), tokens.refreshToken());
    }

    // 리프레시 토큰 처리
    public AuthDto.Response refresh(LoginDto.Request loginRequest) {
        String refreshToken = loginRequest.refreshToken();

        // Refresh Token 유효성 검사
        jwtUtil.validateRefreshToken(refreshToken);
        String id = jwtUtil.extractIdFromRefreshToken(refreshToken);

        // 새로운 Access Token 및 Refresh Token 생성
        String newAccessToken = createAccessToken(id);
        String newRefreshToken = createRefreshToken(id);

        // 기존 Refresh Token 삭제 및 새로 저장
        updateRefreshToken(id, newRefreshToken);

        return new AuthDto.Response(newAccessToken, newRefreshToken);
    }

    // 로그아웃 처리
    public LogoutDto.Response logout(LoginDto.Request loginRequest) {
        String refreshToken = loginRequest.refreshToken();

        // Refresh Token 유효성 검사
        if (!StringUtils.hasText(refreshToken)) {
            throw new RestApiException(ResultCode.INVALID_REFRESH_TOKEN);
        }

        jwtUtil.validateRefreshToken(refreshToken);

        // Refresh Token에서 ID 추출
        String id = jwtUtil.extractIdFromRefreshToken(refreshToken);

        // Redis에서 Refresh Token 삭제
        String storedRefreshToken = refreshTokenRepository.getRefreshToken(id);

        // 실패 조건 처리
        if (storedRefreshToken == null || !storedRefreshToken.equals(refreshToken)) {
            throw new RestApiException(ResultCode.LOGOUT_FAILED);
        }

        // 성공 조건 처리
        refreshTokenRepository.deleteRefreshToken(id);
        return new LogoutDto.Response("Logout successfully");
    }

    // 토큰 생성
    private AuthDto.Tokens generateTokens(String id) {
        String accessToken = createAccessToken(id);
        String refreshToken = createRefreshToken(id);

        return new AuthDto.Tokens(accessToken, refreshToken);
    }

    // Refresh Token 저장
    private void saveRefreshToken(String id, String refreshToken) {
        refreshTokenRepository.saveRefreshToken(id, refreshToken, refreshTokenTimeOut);
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
