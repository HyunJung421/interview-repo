package com.portfolio.interview.security;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.portfolio.interview.repository.RefreshTokenRepository;
import com.portfolio.interview.system.enums.ResultCode;
import com.portfolio.interview.system.exception.RestApiException;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    private final Key key;
    private final long accessTokenExpiration;
    private final long refreshTokenExpiration;
    private final RefreshTokenRepository refreshTokenRepository;

    // 생성자를 통해 SECRET_KEY와 만료 시간을 주입받아 초기화
    public JwtUtil(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.access-token-expiration}") long accessTokenExpiration,
            @Value("${jwt.refresh-token-expiration}") long refreshTokenExpiration,
            RefreshTokenRepository refreshTokenRepository) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    /**
     * JWT 토큰을 생성합니다.
     *
     * @param id 사용자 ID
     * @return JWT 토큰
     */
    public String generateAccessToken(String id) {
        return Jwts.builder()
                .setSubject(id)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpiration))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 리프레시 토큰을 생성합니다.
     *
     * @param id 사용자 ID
     * @return 리프레시 토큰
     */
    public String generateRefreshToken(String id) {
        return Jwts.builder()
                .setSubject(id)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpiration))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * JWT 토큰에서 사용자 ID를 추출합니다.
     *
     * @param token JWT 토큰
     * @return 사용자 ID
     */
    public String extractIdFromJwtToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * Refresh Token에서 사용자 ID를 추출합니다.
     *
     * @param refreshToken Refresh Token
     * @return 사용자 ID
     * @throws RestApiException 토큰이 유효하지 않은 경우
     */
    public String extractIdFromRefreshToken(String refreshToken) {
        if (!StringUtils.hasText(refreshToken)) {
            throw new RestApiException(ResultCode.INVALID_REFRESH_TOKEN);
        }

        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(refreshToken)
                .getBody()
                .getSubject();
    }

    /**
     * JWT 토큰을 검증합니다.
     *
     * @param token JWT 토큰
     * @return true if the token is valid, false otherwise
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            throw mapJwtExceptionToRestApiException(e);
        }
    }

    /**
     * JWT 토큰의 만료를 확인합니다.
     *
     * @param token JWT 토큰
     * @return true if the token is expired, false otherwise
     */
    private RestApiException mapJwtExceptionToRestApiException(Exception e) {
        if (e instanceof io.jsonwebtoken.security.SignatureException) {
            return new RestApiException(ResultCode.INVALID_JWT_SIGNATURE); // JWT 서명 오류
        } else if (e instanceof io.jsonwebtoken.ExpiredJwtException) {
            return new RestApiException(ResultCode.JWT_TOKEN_EXPIRED); // JWT 토큰 만료
        } else if (e instanceof io.jsonwebtoken.UnsupportedJwtException) {
            return new RestApiException(ResultCode.UNSUPPORTED_JWT_TOKEN); // 지원되지 않는 JWT 토큰
        } else if (e instanceof io.jsonwebtoken.MalformedJwtException) {
            return new RestApiException(ResultCode.MALFORMED_JWT_TOKEN); // 잘못된 형식의 JWT 토큰
        } else if (e instanceof IllegalArgumentException) {
            return new RestApiException(ResultCode.INVALID_JWT_TOKEN); // 잘못된 JWT 토큰
        } else {
            return new RestApiException(ResultCode.INTERNAL_SERVER_ERROR); // 기타 서버 오류
        }
    }

    /**
     * Refresh Token을 검증합니다.
     *
     * @param refreshToken Refresh Token
     * @return true if the refresh token is valid, false otherwise
     */
    public boolean validateRefreshToken(String refreshToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(refreshToken);

            // refreshToken에서 사용자 ID 추출
            String id = extractIdFromRefreshToken(refreshToken);

            // Redis에 저장된 refreshToken 검증
            validateStoredRefreshToken(id, refreshToken);
            return true;
        } catch (RestApiException e) {
            throw e; // 이미 RestApiException으로 변환된 경우 그대로 던짐
        } catch (Exception e) {
            throw mapJwtExceptionToRestApiException(e);
        }
    }

    /**
     * Redis에 저장된 Refresh Token을 검증합니다.
     *
     * @param id           사용자 ID
     * @param refreshToken 검증할 Refresh Token
     * @throws RestApiException if the stored refresh token is invalid or not found
     */
    private void validateStoredRefreshToken(String id, String refreshToken) {
        String storedRefreshToken = refreshTokenRepository.getRefreshToken(id);

        if (!StringUtils.hasText(storedRefreshToken)) {
            throw new RestApiException(ResultCode.REFRESH_TOKEN_NOT_FOUND);
        }

        if (!storedRefreshToken.equals(refreshToken)) {
            throw new RestApiException(ResultCode.INVALID_REFRESH_TOKEN);
        }
    }
}