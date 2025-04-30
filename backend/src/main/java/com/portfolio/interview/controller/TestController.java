package com.portfolio.interview.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.portfolio.interview.system.enums.ResultCode;
import com.portfolio.interview.system.exception.RestApiException;

@RestController
public class TestController {
    
    @GetMapping("/test")
    public ResultCode errorTest() {
        throw new RestApiException(ResultCode.TEST_ERROR);
    }
}
