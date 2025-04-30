package com.portfolio.interview.system.advice;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.portfolio.interview.system.dto.ResponseWrapper;
import com.portfolio.interview.system.enums.ResultCode;

@RestControllerAdvice
public class ApiResponseAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
            Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
            ServerHttpResponse response) {

        if(body instanceof ResultCode) {
            ResultCode errorInfo = (ResultCode) body;
            response.setStatusCode(HttpStatus.valueOf(errorInfo.getStatus()));
            return ResponseWrapper.of(errorInfo.getCode(), false, errorInfo.getMessage());
        }

        response.setStatusCode(HttpStatus.OK);
        return ResponseWrapper.of(ResultCode.SUCCESS.getCode(), true, body);
    }
    
}
