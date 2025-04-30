package com.portfolio.interview.system.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResultCode {
    
    // success
    SUCCESS(200, "S20000", "Success"),

    // common
    INTERNAL_SERVER_ERROR(500, "E10000", "Internal server error"),
    TEST_ERROR(400, "E10001", "Test error");

    private final int status;
    private final String code;
    private final String message;
}
