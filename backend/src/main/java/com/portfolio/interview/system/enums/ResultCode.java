package com.portfolio.interview.system.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResultCode {

    // success
    SUCCESS(200, "S20000", "Success"),

    // auth
    INVALID_CREDENTIALS(401, "E00000", "Invalid credentials"),
    INVALID_REFRESH_TOKEN(401, "E00001", "Invalid refresh token"),
    LOGOUT_ERROR(401, "E00002", "Logout error"),
    INVALID_JWT_TOKEN(401, "E00003", "Invalid JWT token"),
    JWT_TOKEN_EXPIRED(401, "E00004", "JWT token has expired"),
    UNSUPPORTED_JWT_TOKEN(401, "E00005", "Unsupported JWT token"),
    MALFORMED_JWT_TOKEN(401, "E00006", "Malformed JWT token"),
    INVALID_JWT_SIGNATURE(401, "E00007", "Invalid JWT signature"),
    LOGOUT_FAILED(500, "E00008", "Logout failed"),
    REFRESH_TOKEN_NOT_FOUND(404, "E00009", "Refresh token not found"),
    UNAUTHORIZED_ACCESS(401, "E00010", "Unauthorized access"),
    UNAUTHORIZED_USER(401, "E00011", "Unauthorized user"),

    // user
    DUPLICATE_ID(400, "E20003", "Id is already taken"),
    DUPLICATE_EMAIL(400, "E20004", "Email is already taken"),
    INVALID_NAME_OR_EMAIL(400, "E20005", "Name or email is incorrect"),
    NON_EXISTENT_ID(404, "E20006", "Id does not exist"),
    INVALID_ID_OR_EMAIL(400, "E20007", "Id or email is incorrect"),
    INVALID_PASSWORD(400, "E20008", "Password is incorrect"),
    EMAIL_SEND_FAILED(500, "E20009", "Email sending failed"),
    USER_SIGNUP_FAILED(500, "E20010", "User signup failed"),
    USER_UPDATE_FAILED(500, "E20011", "User update failed"),
    USER_NOT_FOUND(404, "E20012", "User not found"),
    ROLE_NOT_FOUND(404, "E20013", "Role not found"),

    // common
    INTERNAL_SERVER_ERROR(500, "E10000", "Internal server error"),
    TEST_ERROR(400, "E10001", "Test error");

    private final int status;
    private final String code;
    private final String message;
}
