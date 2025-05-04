package com.portfolio.interview.service;

import java.security.SecureRandom;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.portfolio.interview.dto.UserDto;
import com.portfolio.interview.entity.User;
import com.portfolio.interview.repository.UserRepository;
import com.portfolio.interview.system.enums.ResultCode;
import com.portfolio.interview.system.exception.RestApiException;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;

    /**
     * 회원가입
     * 
     * @param userRequest
     */
    public void signUp(UserDto.Request userRequest) {
        String id = userRequest.id();
        String name = userRequest.name();
        String password = userRequest.password();
        String email = userRequest.email();

        if (userRepository.existsById(id)) {
            throw new RestApiException(ResultCode.DUPLICATE_ID);
        }

        if (userRepository.existsByEmail(email)) {
            throw new RestApiException(ResultCode.DUPLICATE_EMAIL);
        }

        User user = User.builder()
                .id(id)
                .name(name)
                .password(passwordEncoder.encode(password))
                .email(email)
                .build();

        userRepository.save(user);
    }

    /**
     * 로그인 인증
     * 
     * @param id
     * @param password
     * @return
     */
    public boolean authenticate(String id, String password) {
        return userRepository.findById(id)
                .map(user -> passwordEncoder.matches(password, user.getPassword()))
                .orElse(false);
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

        return new UserDto.FindPasswordResponse(true, "Temporary password sent to your email.");
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