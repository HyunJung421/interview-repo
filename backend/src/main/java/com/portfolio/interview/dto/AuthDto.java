package com.portfolio.interview.dto;

public class AuthDto {

    public record Response(String accessToken, String refreshToken) {
    }

    public record Tokens(String accessToken, String refreshToken) {
    }
}
