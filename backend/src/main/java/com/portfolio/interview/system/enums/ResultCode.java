package com.portfolio.interview.system.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResultCode {

    // success
    SUCCESS(200, "S20000", "Success"),

    // auth
    INVALID_AUTH_INFO(401, "E00000", "Invalid id or password"),
    INVALID_REFRESH_TOKEN(401, "E00001", "Invalid Refresh Token"),
    LOGOUT_FAILED(401, "E00002", "Logout failed"),

    // user
    DUPLICATE_ID(400, "E00003", "Id is already taken"),
    DUPLICATE_EMAIL(400, "E00004", "Email is already taken"),
    EMAIL_NOT_FOUND(400, "E00005", "Email not found"),

    // common
    INTERNAL_SERVER_ERROR(500, "E10000", "Internal server error"),
    TEST_ERROR(400, "E10001", "Test error");

    private final int status;
    private final String code;
    private final String message;
}
