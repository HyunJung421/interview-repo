package com.portfolio.interview.service;

import java.security.SecureRandom;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.portfolio.interview.dto.UserDto;
import com.portfolio.interview.entity.Roles;
import com.portfolio.interview.entity.User;
import com.portfolio.interview.entity.UsersRoles;
import com.portfolio.interview.entity.UsersRolesId;
import com.portfolio.interview.repository.RolesRepository;
import com.portfolio.interview.repository.UserRepository;
import com.portfolio.interview.repository.UsersRolesRepository;
import com.portfolio.interview.system.enums.ResultCode;
import com.portfolio.interview.system.exception.RestApiException;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;
    private final UsersRolesRepository usersRolesRepository;
    private final RolesRepository rolesRepository;

    /**
     * 회원가입
     * 
     * @param userRequest
     */
    @Transactional
    public void signUp(UserDto.Request userRequest) {
        validateSignUpRequest(userRequest);

        User savedUser = saveUser(userRequest);

        Roles userRoles = findUserRole();

        saveUserRoleMapping(savedUser, userRoles);
    }

    /**
     * 회원가입 요청 검증
     * 
     * @param userRequest
     */
    private void validateSignUpRequest(UserDto.Request userRequest) {
        if (userRepository.existsById(userRequest.id())) {
            throw new RestApiException(ResultCode.DUPLICATE_ID);
        }

        if (userRepository.existsByEmail(userRequest.email())) {
            throw new RestApiException(ResultCode.DUPLICATE_EMAIL);
        }
    }

    /**
     * 회원가입 요청 처리
     * 
     * @param userRequest
     * @return
     */
    private User saveUser(UserDto.Request userRequest) {
        User user = User.builder()
                .id(userRequest.id())
                .name(userRequest.name())
                .password(passwordEncoder.encode(userRequest.password()))
                .email(userRequest.email())
                .build();

        return userRepository.save(user);
    }

    /**
     * 사용자 역할 찾기
     * 
     * @return
     */
    private Roles findUserRole() {
        var userRole = com.portfolio.interview.system.enums.Roles.USER;

        return rolesRepository.findByName(userRole.name())
                .orElseThrow(() -> new RestApiException(ResultCode.ROLE_NOT_FOUND));
    }

    /**
     * 사용자 역할 매핑 저장
     * 
     * @param savedUser
     * @param userRoles
     */
    private void saveUserRoleMapping(User savedUser, Roles userRoles) {
        UsersRolesId id = new UsersRolesId(savedUser.getSeq(), userRoles.getSeq());

        UsersRoles usersRoles = UsersRoles.builder()
                .id(id)
                .user(savedUser)
                .roles(userRoles)
                .build();

        usersRolesRepository.save(usersRoles);
    }

    /**
     * 아이디 찾기
     * 
     * @param findIdRequest
     * @return
     */
    public UserDto.FindIdResponse findNameAndIdByEmail(UserDto.FindIdRequest findIdRequest) {
        String name = findIdRequest.name();
        String email = findIdRequest.email();

        User user = userRepository.findByNameAndEmail(name, email)
                .orElseThrow(() -> new RestApiException(ResultCode.INVALID_NAME_OR_EMAIL));

        return new UserDto.FindIdResponse(user.getId(), user.getCreatedAt());
    }

    /**
     * 비밀번호 찾기
     * 
     * @param findPasswordRequest
     * @return
     */
    public UserDto.FindPasswordResponse findPasswordByIdAndEmail(UserDto.FindPasswordRequest findPasswordRequest) {
        String id = findPasswordRequest.id();
        String email = findPasswordRequest.email();

        // 사용자 검증
        User user = userRepository.findById(id)
                .filter(u -> u.getEmail().equals(email))
                .orElseThrow(() -> new RestApiException(ResultCode.INVALID_ID_OR_EMAIL));

        // 임시 비밀번호 생성
        String temporaryPassword = generateTemporaryPassword();

        // 비밀번호 업데이트
        user.setPassword(passwordEncoder.encode(temporaryPassword));
        userRepository.save(user);

        // 이메일 전송
        sendTemporaryPasswordEmail(email, temporaryPassword);

        return new UserDto.FindPasswordResponse("Temporary password sent to your email.");
    }

    /**
     * 임시 비밀번호 생성
     * 
     * @param id
     * @param newPassword
     */
    private String generateTemporaryPassword() {
        int length = 10;
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder tempPassword = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int index = secureRandom.nextInt(characters.length());
            tempPassword.append(characters.charAt(index));
        }

        return tempPassword.toString();
    }

    /**
     * 이메일 전송
     * 
     * @param email
     * @param temporaryPassword
     */
    private void sendTemporaryPasswordEmail(String email, String temporaryPassword) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(email);
            helper.setSubject("Temporary Password");
            helper.setText("Your temporary password is: " + temporaryPassword);

            mailSender.send(message);
        } catch (Exception e) {
            throw new RestApiException(ResultCode.EMAIL_SEND_FAILED);
        }
    }
}