package com.developer.smallRoom.application.autocomplete;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

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

    public void bulkSave(String key, List<String> values, double score) {
        for (String value : values) {
            this.save(key, value, score);
        }
    }

    public Optional<Long> findIndexByValue(String key, String value) {
        return Optional.ofNullable(zSetOps.rank(key, value));
    }

    public Set<String> findWordsListByIndex(String key, long start, long end) {
        return zSetOps.range(key, start, end);
    }



}
