package com.portfolio.interview.system.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseWrapper<T> {
    
    private String code;
    private Boolean success;
    private T data;
    private String message;

    // success
    public static <T> ResponseWrapper<T> of(String code, Boolean success, T data) {
        return ResponseWrapper.<T>builder()
                                .code(code)
                                .success(success)
                                .data(data)
                                .build();
    }

    // error
    public static ResponseWrapper<Void> of(String code, Boolean success, String message) {
        return ResponseWrapper.<Void>builder()
                                .code(code)
                                .success(success)
                                .message(message)
                                .build();
    }
}
