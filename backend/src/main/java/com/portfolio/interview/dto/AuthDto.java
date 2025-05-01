package com.portfolio.interview.dto;

public class AuthDto {

    public record Response(String accessToken, String refreshToken) {
    }
}
