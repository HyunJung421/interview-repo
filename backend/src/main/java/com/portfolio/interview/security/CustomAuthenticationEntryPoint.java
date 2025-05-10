package com.portfolio.interview.security;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.portfolio.interview.system.dto.ResponseWrapper;
import com.portfolio.interview.system.enums.ResultCode;
import com.portfolio.interview.system.exception.RestApiException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 인증 실패 시 호출되는 메서드입니다.
     *
     * @param request       HttpServletRequest 객체
     * @param response      HttpServletResponse 객체
     * @param authException AuthenticationException 객체
     * @throws IOException IOException
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException {
        log.info("Authentication failed: {}", authException != null ? authException.getMessage() : "Unknown error");

        // 예외 정보 가져오기
        RestApiException jwtException = (RestApiException) request.getAttribute("jwtException");
        ResultCode resultCode = jwtException != null ? jwtException.getErrorCode() : ResultCode.UNAUTHORIZED_ACCESS;

        // 응답 생성 및 반환
        writeErrorResponse(response, resultCode);
    }

    /**
     * 에러 응답을 작성합니다.
     *
     * @param response   HttpServletResponse 객체
     * @param resultCode ResultCode 객체
     * @throws IOException IOException
     */
    private void writeErrorResponse(HttpServletResponse response, ResultCode resultCode) throws IOException {
        ResponseWrapper<Object> responseWrapper = ResponseWrapper.builder()
                .code(resultCode.getCode())
                .success(false)
                .message(resultCode.getMessage())
                .build();

        response.setContentType("application/json");
        response.setStatus(resultCode.getStatus());
        response.getWriter().write(objectMapper.writeValueAsString(responseWrapper));
    }
}