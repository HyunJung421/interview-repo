package com.portfolio.interview.dto;

public class AuthDto {

    public record LoginRequest(String id, String password) {
    }

    public record RefreshRequest(String refreshToken) {
    }

    public record LogoutRequest(String refreshToken) {
    }

    public record LogoutResponse(String message) {
    }

    public record Response(String accessToken, String refreshToken) {
    }

    public record Tokens(String accessToken, String refreshToken) {
    }

}
