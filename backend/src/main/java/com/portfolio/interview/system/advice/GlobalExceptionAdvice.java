package com.portfolio.interview.system.advice;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.portfolio.interview.system.enums.ResultCode;
import com.portfolio.interview.system.exception.RestApiException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionAdvice {

    @ExceptionHandler(RestApiException.class)
    public ResultCode handleRestApiException(RestApiException e) {

        ResultCode errorCode = e.getErrorCode();

        log.error("          :::::::::: RestApiException [ code: {}, message: {} ] ::::::::::", errorCode.getCode(),
                errorCode.getMessage());

        return errorCode;
    }

    @ExceptionHandler(Exception.class)
    public ResultCode handleAllException(Exception e) {
        log.error("          :::::::::: Exception [ {} ] ::::::::::", e);
        log.error("          :::::::::: Exception [ message: {} ] ::::::::::", e.getMessage());

        return ResultCode.INTERNAL_SERVER_ERROR;
    }
}
