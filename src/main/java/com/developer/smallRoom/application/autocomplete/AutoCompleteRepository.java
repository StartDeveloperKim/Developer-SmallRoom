package com.developer.smallRoom.application.autocomplete;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

@Repository
public class AutoCompleteRepository {

    private final ZSetOperations<String, String> zSetOps;

    @Autowired
    public AutoCompleteRepository(RedisTemplate<String, String> redisTemplate) {
        this.zSetOps = redisTemplate.opsForZSet();
    }

    public void save(String key, String value, double score) {
        zSetOps.add(key, value, score);
    }

    public Long findIndexByValue(String key, String value) {
        return zSetOps.rank(key, value);
    }
}
