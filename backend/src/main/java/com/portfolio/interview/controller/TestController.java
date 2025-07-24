package com.portfolio.interview.controller;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.portfolio.interview.system.enums.ResultCode;
import com.portfolio.interview.system.exception.RestApiException;

@RestController
@Hidden // Swagger UI에서 숨김
public class TestController {
    
    @GetMapping("/test")
    public ResultCode errorTest() {
        throw new RestApiException(ResultCode.TEST_ERROR);
    }
}
