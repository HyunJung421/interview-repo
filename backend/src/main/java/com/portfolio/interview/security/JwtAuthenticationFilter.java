package com.portfolio.interview.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.portfolio.interview.service.CustomUserDetailsService;
import com.portfolio.interview.system.exception.RestApiException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    /**
     * JWT 인증 필터
     *
     * @param request  HttpServletRequest 객체
     * @param response HttpServletResponse 객체
     * @param chain    FilterChain 객체
     * @throws ServletException ServletException
     * @throws IOException      IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        // JWT 토큰을 헤더에서 가져오기
        String token = resolveToken(request);

        // 토큰이 유효한 경우 인증 정보 설정
        if (token != null) {
            try {
                jwtUtil.validateToken(token);
                String id = jwtUtil.extractIdFromJwtToken(token);
                UserDetails userDetails = userDetailsService.loadUserByUsername(id);

                var authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
                        userDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (RestApiException e) {
                request.setAttribute("jwtException", e);
            }
        }

        chain.doFilter(request, response);
    }

    /**
     * Authorization 헤더에서 JWT 토큰을 추출합니다.
     *
     * @param request HttpServletRequest 객체
     * @return JWT 토큰
     */
    private String resolveToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");

        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }

        return null;
    }

}