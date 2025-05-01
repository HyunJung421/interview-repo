package com.portfolio.interview.dto;

public class LoginDto {

    public record Request(String id, String password, String refreshToken) {
    }
}
