package com.portfolio.interview.service;

import com.portfolio.interview.entity.User;
import com.portfolio.interview.repository.UserRepository;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        User user = userRepository.findById(name)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + name));
        return new CustomUserDetails(user);
    }
}