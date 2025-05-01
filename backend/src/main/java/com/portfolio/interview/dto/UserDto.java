package com.portfolio.interview.dto;

public record UserDto() {
    public record Request(String id, String name, String password, String email) {
    }
}
