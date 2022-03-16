package com.example.demo.redis;

import com.example.demo.entity.Invest;
import com.example.demo.entity.InvestProduct;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RList;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisProvider {

    private final RedissonClient redissonClient;
    final String key = "Invest";

    public RMap<String, InvestProduct> map() {
        return redissonClient.getMap(key);
    }

    public RedissonClient getRedissonClient() {
        return redissonClient;
    }
}
