package com.portfolio.interview.service;

import com.portfolio.interview.entity.User;
import com.portfolio.interview.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void signUp(String id, String name, String password, String email) {
        if (userRepository.existsById(id)) {
            throw new IllegalArgumentException("Id is already taken.");
        }

        if (userRepository.existsByName(name)) {
            throw new IllegalArgumentException("Name is already taken.");
        }

        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email is already signUped.");
        }

        User user = User.builder()
                .id(id)
                .name(name)
                .password(passwordEncoder.encode(password))
                .email(email)
                .build();

        userRepository.save(user);
    }

    public boolean authenticate(String id, String password) {
        return userRepository.findById(id)
                .map(user -> passwordEncoder.matches(password, user.getPassword()))
                .orElse(false);
    }
}