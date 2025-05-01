package com.portfolio.interview.repository;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RefreshTokenRepository {

    private final RedisTemplate<String, String> redisTemplate;

    public RefreshTokenRepository(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void saveRefreshToken(String id, String refreshToken, long expirationTime) {
        redisTemplate.opsForValue().set(id, refreshToken, expirationTime, TimeUnit.MILLISECONDS);
    }

    public String getRefreshToken(String id) {
        return redisTemplate.opsForValue().get(id);
    }

    public void deleteRefreshToken(String id) {
        redisTemplate.delete(id);
    }

}
