package com.deiz0n.studfit.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class RedisIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Test
    void shouldConnectToRedis() {
        redisTemplate.opsForValue().set("key", "value");

        String valor = redisTemplate.opsForValue().get("key");

        assertThat(valor).isEqualTo("value");
    }
}
