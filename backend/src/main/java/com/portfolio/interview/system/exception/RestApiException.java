package com.portfolio.interview.system.exception;

import com.portfolio.interview.system.enums.ResultCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RestApiException extends RuntimeException {
    private final ResultCode errorCode;
}
