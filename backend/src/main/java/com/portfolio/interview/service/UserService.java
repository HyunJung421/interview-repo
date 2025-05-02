package com.portfolio.interview.service;

import com.portfolio.interview.dto.UserDto;
import com.portfolio.interview.entity.User;
import com.portfolio.interview.repository.UserRepository;
import com.portfolio.interview.system.enums.ResultCode;
import com.portfolio.interview.system.exception.RestApiException;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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

        User user = userRepository.findByEmailAndName(name, email)
                .orElseThrow(() -> new RestApiException(ResultCode.EMAIL_NOT_FOUND));

        return new UserDto.FindIdResponse(user.getId(), user.getCreatedAt());
    }
}