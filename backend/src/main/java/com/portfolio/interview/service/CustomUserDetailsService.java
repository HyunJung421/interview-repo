package com.portfolio.interview.service;

import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.portfolio.interview.entity.User;
import com.portfolio.interview.repository.UserRepository;
import com.portfolio.interview.security.CustomUserDetails;
import com.portfolio.interview.system.enums.ResultCode;
import com.portfolio.interview.system.exception.RestApiException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * 사용자 ID로 사용자 정보를 조회하여 UserDetails 객체를 반환합니다.
     *
     * @param id 사용자 ID
     * @return UserDetails 객체
     * @throws UsernameNotFoundException 사용자를 찾을 수 없는 경우
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RestApiException(ResultCode.USER_NOT_FOUND));

        return new CustomUserDetails(user, user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoles().getName()))
                .collect(Collectors.toList()));
    }
}