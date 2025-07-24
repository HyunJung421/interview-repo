package com.portfolio.interview.service;

import com.portfolio.interview.dto.AuthDto;
import com.portfolio.interview.entity.User;
import com.portfolio.interview.repository.RefreshTokenRepository;
import com.portfolio.interview.security.CustomUserDetails;
import com.portfolio.interview.security.JwtUtil;
import com.portfolio.interview.system.enums.ResultCode;
import com.portfolio.interview.system.exception.RestApiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    private final String TEST_USER_ID = "testuser";
    private final String TEST_PASSWORD = "password123";
    private final String TEST_ENCODED_PASSWORD = "encodedPassword123";
    private final String TEST_ACCESS_TOKEN = "test.access.token";
    private final String TEST_REFRESH_TOKEN = "test.refresh.token";
    private final String TEST_NEW_ACCESS_TOKEN = "test.new.access.token";
    private final String TEST_NEW_REFRESH_TOKEN = "test.new.refresh.token";
    private final Long REFRESH_TOKEN_TIMEOUT = 86400L;

    private User testUser;
    private CustomUserDetails userDetails;

    @BeforeEach
    void setUp() {
        // Set up test user
        testUser = new User();
        testUser.setId(TEST_USER_ID);
        testUser.setPassword(TEST_ENCODED_PASSWORD);

        // Set up authorities
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        // Set up user details
        userDetails = new CustomUserDetails(testUser, authorities);

        // Set refresh token timeout using reflection
        ReflectionTestUtils.setField(authService, "refreshTokenTimeOut", REFRESH_TOKEN_TIMEOUT);
    }

    @Test
    @DisplayName("로그인 성공 테스트")
    void login_Success() {
        // Arrange
        AuthDto.LoginRequest loginRequest = new AuthDto.LoginRequest(TEST_USER_ID, TEST_PASSWORD);
        
        when(userDetailsService.loadUserByUsername(TEST_USER_ID)).thenReturn(userDetails);
        when(passwordEncoder.matches(TEST_PASSWORD, TEST_ENCODED_PASSWORD)).thenReturn(true);
        when(jwtUtil.generateAccessToken(TEST_USER_ID)).thenReturn(TEST_ACCESS_TOKEN);
        when(jwtUtil.generateRefreshToken(TEST_USER_ID)).thenReturn(TEST_REFRESH_TOKEN);
        
        // Act
        AuthDto.Response response = authService.login(loginRequest);
        
        // Assert
        assertThat(response).isNotNull();
        assertThat(response.accessToken()).isEqualTo(TEST_ACCESS_TOKEN);
        assertThat(response.refreshToken()).isEqualTo(TEST_REFRESH_TOKEN);
        
        verify(refreshTokenRepository).saveRefreshToken(TEST_USER_ID, TEST_REFRESH_TOKEN, REFRESH_TOKEN_TIMEOUT);
    }

    @Test
    @DisplayName("로그인 실패 - 사용자를 찾을 수 없음")
    void login_UserNotFound() {
        // Arrange
        AuthDto.LoginRequest loginRequest = new AuthDto.LoginRequest(TEST_USER_ID, TEST_PASSWORD);
        
        when(userDetailsService.loadUserByUsername(TEST_USER_ID)).thenReturn(null);
        
        // Act & Assert
        assertThatThrownBy(() -> authService.login(loginRequest))
            .isInstanceOf(RestApiException.class)
            .hasFieldOrPropertyWithValue("errorCode", ResultCode.USER_NOT_FOUND);
        
        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(jwtUtil, never()).generateAccessToken(anyString());
        verify(jwtUtil, never()).generateRefreshToken(anyString());
    }

    @Test
    @DisplayName("로그인 실패 - 비밀번호 불일치")
    void login_InvalidPassword() {
        // Arrange
        AuthDto.LoginRequest loginRequest = new AuthDto.LoginRequest(TEST_USER_ID, TEST_PASSWORD);
        
        when(userDetailsService.loadUserByUsername(TEST_USER_ID)).thenReturn(userDetails);
        when(passwordEncoder.matches(TEST_PASSWORD, TEST_ENCODED_PASSWORD)).thenReturn(false);
        
        // Act & Assert
        assertThatThrownBy(() -> authService.login(loginRequest))
            .isInstanceOf(RestApiException.class)
            .hasFieldOrPropertyWithValue("errorCode", ResultCode.INVALID_PASSWORD);
        
        verify(jwtUtil, never()).generateAccessToken(anyString());
        verify(jwtUtil, never()).generateRefreshToken(anyString());
    }

    @Test
    @DisplayName("토큰 갱신 성공 테스트")
    void refresh_Success() {
        // Arrange
        AuthDto.RefreshRequest refreshRequest = new AuthDto.RefreshRequest(TEST_REFRESH_TOKEN);
        
        when(jwtUtil.validateRefreshToken(TEST_REFRESH_TOKEN)).thenReturn(true);
        when(jwtUtil.extractIdFromRefreshToken(TEST_REFRESH_TOKEN)).thenReturn(TEST_USER_ID);
        when(jwtUtil.generateAccessToken(TEST_USER_ID)).thenReturn(TEST_NEW_ACCESS_TOKEN);
        when(jwtUtil.generateRefreshToken(TEST_USER_ID)).thenReturn(TEST_NEW_REFRESH_TOKEN);
        
        // Act
        AuthDto.Response response = authService.refresh(refreshRequest);
        
        // Assert
        assertThat(response).isNotNull();
        assertThat(response.accessToken()).isEqualTo(TEST_NEW_ACCESS_TOKEN);
        assertThat(response.refreshToken()).isEqualTo(TEST_NEW_REFRESH_TOKEN);
        
        verify(refreshTokenRepository).deleteRefreshToken(TEST_USER_ID);
        verify(refreshTokenRepository).saveRefreshToken(TEST_USER_ID, TEST_NEW_REFRESH_TOKEN, REFRESH_TOKEN_TIMEOUT);
    }

    @Test
    @DisplayName("토큰 갱신 실패 - 유효하지 않은 리프레시 토큰")
    void refresh_InvalidRefreshToken() {
        // Arrange
        AuthDto.RefreshRequest refreshRequest = new AuthDto.RefreshRequest(TEST_REFRESH_TOKEN);
        
        when(jwtUtil.validateRefreshToken(TEST_REFRESH_TOKEN))
            .thenThrow(new RestApiException(ResultCode.INVALID_REFRESH_TOKEN));
        
        // Act & Assert
        assertThatThrownBy(() -> authService.refresh(refreshRequest))
            .isInstanceOf(RestApiException.class)
            .hasFieldOrPropertyWithValue("errorCode", ResultCode.INVALID_REFRESH_TOKEN);
        
        verify(jwtUtil, never()).extractIdFromRefreshToken(anyString());
        verify(jwtUtil, never()).generateAccessToken(anyString());
        verify(jwtUtil, never()).generateRefreshToken(anyString());
    }

    @Test
    @DisplayName("로그아웃 성공 테스트")
    void logout_Success() {
        // Arrange
        AuthDto.LogoutRequest logoutRequest = new AuthDto.LogoutRequest(TEST_REFRESH_TOKEN);
        
        when(jwtUtil.validateRefreshToken(TEST_REFRESH_TOKEN)).thenReturn(true);
        when(jwtUtil.extractIdFromRefreshToken(TEST_REFRESH_TOKEN)).thenReturn(TEST_USER_ID);
        when(refreshTokenRepository.getRefreshToken(TEST_USER_ID)).thenReturn(TEST_REFRESH_TOKEN);
        
        // Act
        AuthDto.LogoutResponse response = authService.logout(logoutRequest);
        
        // Assert
        assertThat(response).isNotNull();
        assertThat(response.message()).isEqualTo("Logout successfully");
        
        verify(refreshTokenRepository).deleteRefreshToken(TEST_USER_ID);
    }

    @Test
    @DisplayName("로그아웃 실패 - 빈 리프레시 토큰")
    void logout_EmptyRefreshToken() {
        // Arrange
        AuthDto.LogoutRequest logoutRequest = new AuthDto.LogoutRequest("");
        
        // Act & Assert
        assertThatThrownBy(() -> authService.logout(logoutRequest))
            .isInstanceOf(RestApiException.class)
            .hasFieldOrPropertyWithValue("errorCode", ResultCode.INVALID_REFRESH_TOKEN);
        
        verify(jwtUtil, never()).validateRefreshToken(anyString());
        verify(jwtUtil, never()).extractIdFromRefreshToken(anyString());
        verify(refreshTokenRepository, never()).getRefreshToken(anyString());
        verify(refreshTokenRepository, never()).deleteRefreshToken(anyString());
    }

    @Test
    @DisplayName("로그아웃 실패 - 저장된 토큰과 불일치")
    void logout_TokenMismatch() {
        // Arrange
        AuthDto.LogoutRequest logoutRequest = new AuthDto.LogoutRequest(TEST_REFRESH_TOKEN);
        String storedToken = "different.refresh.token";
        
        when(jwtUtil.validateRefreshToken(TEST_REFRESH_TOKEN)).thenReturn(true);
        when(jwtUtil.extractIdFromRefreshToken(TEST_REFRESH_TOKEN)).thenReturn(TEST_USER_ID);
        when(refreshTokenRepository.getRefreshToken(TEST_USER_ID)).thenReturn(storedToken);
        
        // Act & Assert
        assertThatThrownBy(() -> authService.logout(logoutRequest))
            .isInstanceOf(RestApiException.class)
            .hasFieldOrPropertyWithValue("errorCode", ResultCode.LOGOUT_FAILED);
        
        verify(refreshTokenRepository, never()).deleteRefreshToken(anyString());
    }
}