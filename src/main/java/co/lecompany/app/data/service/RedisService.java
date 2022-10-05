package co.lecompany.app.data.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RedisService {

    @Qualifier("CommonRedis")
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    public void add(String key, String data, Long expiredTime) {
        redisTemplate.opsForValue().set(key, data, expiredTime, TimeUnit.SECONDS);
    }

    public String get(String key) throws JsonProcessingException {
        return objectMapper.writeValueAsString(redisTemplate.opsForValue().get(key));
    }

    public void remove(String key) {
        redisTemplate.delete(key);
    }
}
