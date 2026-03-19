package com.projects.system.urlshortener.service;

import com.projects.system.urlshortener.entity.UrlMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
public class RedisDataService {
    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public RedisDataService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public String getLongUrl(String shortCode) {
        String key = getKey(shortCode);
        return (String) redisTemplate.opsForHash().get(key, "long_url");
    }

    public void writeLongUrl(UrlMapping urlMapping) {
        String key = getKey(urlMapping.getShortCode());

        redisTemplate.opsForHash().put(key, "long_url", urlMapping.getLongUrl());
        redisTemplate.opsForHash().put(key, "hits", urlMapping.getHits() + 1L);
        if (urlMapping.getExpireAt() != null && urlMapping.getExpireAt().isAfter(Instant.now())) {
            Duration ttl = Duration.between(Instant.now(), urlMapping.getExpireAt());
            redisTemplate.expire(key, ttl);
        }
    }

    @Async
    public void incrementHitCount(String shortCode) {
        redisTemplate.opsForHash().increment(getKey(shortCode), "hits", 1);
    }

    @Async
    public void removeShortCode(String shortCode) {
        redisTemplate.opsForHash().delete(getKey(shortCode));
    }

    private String getKey(String shortCode) {
        return "url:" + shortCode;
    }
}
