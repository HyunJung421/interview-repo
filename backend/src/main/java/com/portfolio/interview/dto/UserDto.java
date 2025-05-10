package com.portfolio.interview.dto;

import java.time.LocalDateTime;

public record UserDto() {
    public record Request(String id, String name, String password, String email) {
    }

    public record FindIdRequest(String name, String email) {
    }

    public record FindIdResponse(String id, LocalDateTime createdAt) {
    }

    public record FindPasswordRequest(String id, String email) {
    }

    public record FindPasswordResponse(String message) {
    }
}
