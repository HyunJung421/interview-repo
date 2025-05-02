package com.portfolio.interview.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.portfolio.interview.dto.SignUpDto;
import com.portfolio.interview.dto.UserDto;
import com.portfolio.interview.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    private final UserService userService;

    /**
     * 회원가입
     * 
     * @param userRequest
     * @return
     */
    @PostMapping("/signUp")
    public SignUpDto.Response signUp(@RequestBody UserDto.Request userRequest) {
        log.info("Sign-up request received for user: {}", userRequest.id());
        userService.signUp(userRequest);
        return new SignUpDto.Response(true, "User signUp successfully");
    }

    /**
     * 아이디 찾기
     * 
     * @param findIdRequest
     * @return
     */
    @PostMapping("/find-id")
    public UserDto.FindIdResponse findId(@RequestBody UserDto.FindIdRequest findIdRequest) {
        log.info("Find ID request received for email: {}", findIdRequest.email());
        return userService.findNameAndIdByEmail(findIdRequest);
    }
}
