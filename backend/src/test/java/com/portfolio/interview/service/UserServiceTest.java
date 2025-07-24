package com.portfolio.interview.service;

import com.portfolio.interview.dto.UserDto;
import com.portfolio.interview.entity.Roles;
import com.portfolio.interview.entity.User;
import com.portfolio.interview.entity.UsersRoles;
import com.portfolio.interview.repository.RolesRepository;
import com.portfolio.interview.repository.UserRepository;
import com.portfolio.interview.repository.UsersRolesRepository;
import com.portfolio.interview.system.enums.ResultCode;
import com.portfolio.interview.system.exception.RestApiException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private UsersRolesRepository usersRolesRepository;

    @Mock
    private RolesRepository rolesRepository;

    @Mock
    private MimeMessage mimeMessage;

    @Mock
    private MimeMessageHelper mimeMessageHelper;

    @InjectMocks
    private UserService userService;

    private UserDto.Request userRequest;
    private User user;
    private Roles userRole;

    @BeforeEach
    void setUp() {
        // Setup common test data
        userRequest = new UserDto.Request("testId", "Test User", "password123", "test@example.com");
        
        user = User.builder()
                .seq(1L)
                .id(userRequest.id())
                .name(userRequest.name())
                .password("encodedPassword")
                .email(userRequest.email())
                .createdAt(LocalDateTime.now())
                .build();
        
        userRole = new Roles();
        userRole.setSeq(1L);
        userRole.setName("USER");
        userRole.setDescription("Regular user role");
    }

    @Test
    @DisplayName("회원가입 성공 테스트")
    void signUp_Success() {
        // Given
        when(userRepository.existsById(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(rolesRepository.findByName(anyString())).thenReturn(Optional.of(userRole));
        
        // When
        userService.signUp(userRequest);
        
        // Then
        verify(userRepository).existsById(userRequest.id());
        verify(userRepository).existsByEmail(userRequest.email());
        verify(passwordEncoder).encode(userRequest.password());
        verify(userRepository).save(any(User.class));
        verify(rolesRepository).findByName("USER");
        verify(usersRolesRepository).save(any(UsersRoles.class));
    }

    @Test
    @DisplayName("회원가입 실패 테스트 - 중복 ID")
    void signUp_FailDuplicateId() {
        // Given
        when(userRepository.existsById(anyString())).thenReturn(true);
        
        // When & Then
        assertThatThrownBy(() -> userService.signUp(userRequest))
            .isInstanceOf(RestApiException.class)
            .hasFieldOrPropertyWithValue("errorCode", ResultCode.DUPLICATE_ID);
        
        verify(userRepository).existsById(userRequest.id());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("회원가입 실패 테스트 - 중복 이메일")
    void signUp_FailDuplicateEmail() {
        // Given
        when(userRepository.existsById(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(true);
        
        // When & Then
        assertThatThrownBy(() -> userService.signUp(userRequest))
            .isInstanceOf(RestApiException.class)
            .hasFieldOrPropertyWithValue("errorCode", ResultCode.DUPLICATE_EMAIL);
        
        verify(userRepository).existsById(userRequest.id());
        verify(userRepository).existsByEmail(userRequest.email());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("아이디 찾기 성공 테스트")
    void findNameAndIdByEmail_Success() {
        // Given
        UserDto.FindIdRequest findIdRequest = new UserDto.FindIdRequest("Test User", "test@example.com");
        when(userRepository.findByNameAndEmail(anyString(), anyString())).thenReturn(Optional.of(user));
        
        // When
        UserDto.FindIdResponse response = userService.findNameAndIdByEmail(findIdRequest);
        
        // Then
        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(user.getId());
        assertThat(response.createdAt()).isEqualTo(user.getCreatedAt());
        verify(userRepository).findByNameAndEmail(findIdRequest.name(), findIdRequest.email());
    }

    @Test
    @DisplayName("아이디 찾기 실패 테스트 - 사용자 없음")
    void findNameAndIdByEmail_UserNotFound() {
        // Given
        UserDto.FindIdRequest findIdRequest = new UserDto.FindIdRequest("Test User", "test@example.com");
        when(userRepository.findByNameAndEmail(anyString(), anyString())).thenReturn(Optional.empty());
        
        // When & Then
        assertThatThrownBy(() -> userService.findNameAndIdByEmail(findIdRequest))
            .isInstanceOf(RestApiException.class)
            .hasFieldOrPropertyWithValue("errorCode", ResultCode.INVALID_NAME_OR_EMAIL);
        
        verify(userRepository).findByNameAndEmail(findIdRequest.name(), findIdRequest.email());
    }

    @Test
    @DisplayName("비밀번호 찾기 성공 테스트")
    void findPasswordByIdAndEmail_Success() throws Exception {
        // Given
        UserDto.FindPasswordRequest findPasswordRequest = new UserDto.FindPasswordRequest("testId", "test@example.com");
        when(userRepository.findById(anyString())).thenReturn(Optional.of(user));
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        doNothing().when(mailSender).send(any(MimeMessage.class));
        
        // When
        UserDto.FindPasswordResponse response = userService.findPasswordByIdAndEmail(findPasswordRequest);
        
        // Then
        assertThat(response).isNotNull();
        assertThat(response.message()).isEqualTo("Temporary password sent to your email.");
        verify(userRepository).findById(findPasswordRequest.id());
        verify(passwordEncoder).encode(anyString());
        verify(userRepository).save(user);
        verify(mailSender).createMimeMessage();
        verify(mailSender).send(any(MimeMessage.class));
    }

    @Test
    @DisplayName("비밀번호 찾기 실패 테스트 - 사용자 없음")
    void findPasswordByIdAndEmail_UserNotFound() {
        // Given
        UserDto.FindPasswordRequest findPasswordRequest = new UserDto.FindPasswordRequest("testId", "test@example.com");
        when(userRepository.findById(anyString())).thenReturn(Optional.empty());
        
        // When & Then
        assertThatThrownBy(() -> userService.findPasswordByIdAndEmail(findPasswordRequest))
            .isInstanceOf(RestApiException.class)
            .hasFieldOrPropertyWithValue("errorCode", ResultCode.INVALID_ID_OR_EMAIL);
        
        verify(userRepository).findById(findPasswordRequest.id());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("비밀번호 찾기 실패 테스트 - 이메일 불일치")
    void findPasswordByIdAndEmail_EmailMismatch() {
        // Given
        UserDto.FindPasswordRequest findPasswordRequest = new UserDto.FindPasswordRequest("testId", "wrong@example.com");
        User userWithDifferentEmail = User.builder()
                .seq(1L)
                .id("testId")
                .name("Test User")
                .password("encodedPassword")
                .email("test@example.com") // Different from request
                .build();
        
        when(userRepository.findById(anyString())).thenReturn(Optional.of(userWithDifferentEmail));
        
        // When & Then
        assertThatThrownBy(() -> userService.findPasswordByIdAndEmail(findPasswordRequest))
            .isInstanceOf(RestApiException.class)
            .hasFieldOrPropertyWithValue("errorCode", ResultCode.INVALID_ID_OR_EMAIL);
        
        verify(userRepository).findById(findPasswordRequest.id());
        verify(userRepository, never()).save(any(User.class));
    }
}