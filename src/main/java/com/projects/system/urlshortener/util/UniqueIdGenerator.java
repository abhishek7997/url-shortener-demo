package com.projects.system.urlshortener.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class UniqueIdGenerator {
    private static final String COUNTER_KEY = "url_counter:counter";
    private final StringRedisTemplate redisTemplate;

    @Autowired
    public UniqueIdGenerator(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
    Generate unique ID based on redis counter
    */
    public long get() {
        Long id = redisTemplate.opsForValue().increment(COUNTER_KEY);
        if (id == null) {
            throw new IllegalStateException("Unable to generate unique ID from Redis");
        }
        return id;
    }
}
