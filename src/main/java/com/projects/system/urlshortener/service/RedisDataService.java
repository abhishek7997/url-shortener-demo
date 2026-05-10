package com.projects.system.urlshortener.service;

import com.projects.system.urlshortener.entity.UrlMapping;
import io.lettuce.core.RedisCommandTimeoutException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ThreadLocalRandom;

@Service
@Slf4j
public class RedisDataService {
    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public RedisDataService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public String getLongUrl(String shortCode) {
        String key = getKey(shortCode);
        String longUrl = null;
        try {
            longUrl = (String) redisTemplate.opsForHash().get(key, "long_url");
        } catch (RedisCommandTimeoutException e) {
            log.error("Redis timeout while deleting key {}", key, e);
        } catch (RedisConnectionFailureException e) {
            log.error("Redis unavailable", e);
        } catch (Exception e) {
            log.error("Unknown error", e);
        }
        return longUrl;
    }

    public void writeLongUrl(UrlMapping urlMapping) {
        String key = getKey(urlMapping.getShortCode());

        redisTemplate.opsForHash().put(key, "long_url", urlMapping.getLongUrl());
        redisTemplate.opsForHash().put(key, "hits", urlMapping.getHits() + 1L);
        Duration ttl;
        if (urlMapping.getExpireAt() != null && urlMapping.getExpireAt().isAfter(Instant.now())) {
            ttl = Duration.between(Instant.now(), urlMapping.getExpireAt());
        } else {
            long jitter = ThreadLocalRandom.current().nextLong(0, 2 * 60 * 60); // 2 hours jitter
            ttl = Duration.ofSeconds(Duration.ofDays(3).getSeconds() + jitter);
        }

        redisTemplate.expire(key, ttl);
    }

    @Async
    public void incrementHitCount(String shortCode) {
        redisTemplate.opsForHash().increment(getKey(shortCode), "hits", 1);
    }

    @Async
    public void removeShortCode(String shortCode) {
        String key = getKey(shortCode);
        try {
            redisTemplate.delete(key);
        } catch (RedisCommandTimeoutException e) {
            log.error("Redis timeout while deleting key {}", key, e);
        } catch (RedisConnectionFailureException e) {
            log.error("Redis unavailable", e);
        }
    }

    private String getKey(String shortCode) {
        return "url:" + shortCode;
    }
}
