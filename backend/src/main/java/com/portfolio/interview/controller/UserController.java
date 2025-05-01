package com.portfolio.interview.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.portfolio.interview.dto.UserDto;
import com.portfolio.interview.service.UserService;
import com.portfolio.interview.system.enums.ResultCode;
import com.portfolio.interview.system.exception.RestApiException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    /**
     * 회원가입
     * 
     * @param id
     * @param password
     * @param email
     * @author taekwon
     * @return
     */
    @PostMapping("/signUp")
    public Boolean signUp(@RequestBody UserDto.Request userRequest) {
        String id = userRequest.id();
        String name = userRequest.name();
        String password = userRequest.password();
        String email = userRequest.email();

        try {
            userService.signUp(id, name, password, email);
            return true;
        } catch (IllegalArgumentException e) {
            throw new RestApiException(ResultCode.SIGNUP_FAILED);
        }
    }

    @PostMapping("/find-id")
    public UserDto.FindIdResponse findId(@RequestBody UserDto.FindIdRequest findIdRequest) {
        String name = findIdRequest.name();
        String email = findIdRequest.email();

        try {
            return userService.findNameAndIdByEmail(name, email);
        } catch (IllegalArgumentException e) {
            throw new RestApiException(ResultCode.EMAIL_NOT_FOUND);
        }
    }
}
