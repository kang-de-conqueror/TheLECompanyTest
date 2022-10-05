package co.lecompany.app.config.redis;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
@EnableCaching
@Slf4j
public class RedisCacheConfig {

    @Autowired
    private RedisCacheConfigProperties redisCacheConfigProperties;

    @Bean
    public RedisTemplate<String, Object> redisTemplate(
            @Qualifier(value = "CommonRedis") RedisConnectionFactory connectionFactory
    ) {
        log.info("Create RedisTemplate connection pool...");
        final RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setValueSerializer(new GenericToStringSerializer<>(Object.class));

        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());

        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new StringRedisSerializer());

        template.setConnectionFactory(connectionFactory);
        return template;
    }

    @Bean(name = "CommonRedis")
    @Primary
    public LettuceConnectionFactory redisConnectionFactory(GenericObjectPoolConfig genericObjectPoolConfig) {
        log.info("Create RedisConnectionFactory connection pool to Redis server: {} - port: {} - pass: {}",
                redisCacheConfigProperties.getCommonHost(),
                redisCacheConfigProperties.getCommonPort(),
                redisCacheConfigProperties.getCommonPassword());

        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();

        redisStandaloneConfiguration.setDatabase(redisCacheConfigProperties.getDatabase() == null ? 0 : redisStandaloneConfiguration.getDatabase());
        redisStandaloneConfiguration.setHostName(redisCacheConfigProperties.getCommonHost());
        redisStandaloneConfiguration.setPort(redisCacheConfigProperties.getCommonPort());
        redisStandaloneConfiguration.setPassword(RedisPassword.of(redisCacheConfigProperties.getCommonPassword()));

        LettuceClientConfiguration clientConfiguration = LettucePoolingClientConfiguration.builder()
                .commandTimeout(Duration.ofMillis(redisCacheConfigProperties.getTimeout()))
                .poolConfig(genericObjectPoolConfig).build();

        return new LettuceConnectionFactory(redisStandaloneConfiguration, clientConfiguration);
    }

    @Bean(name = "genericPoolConfig")
    @Primary
    public GenericObjectPoolConfig genericPoolConfig() {
        log.info("Init Redis connection pool MaxActive :{}", redisCacheConfigProperties.getCommonMaxActive());

        GenericObjectPoolConfig genericObjectPoolConfig = new GenericObjectPoolConfig();

        genericObjectPoolConfig.setMaxIdle(redisCacheConfigProperties.getCommonMaxIdle());
        genericObjectPoolConfig.setMinIdle(redisCacheConfigProperties.getCommonMinIdle());
        genericObjectPoolConfig.setMaxTotal(redisCacheConfigProperties.getCommonMaxActive());
        genericObjectPoolConfig.setMaxWait(Duration.ofMillis(redisCacheConfigProperties.getCommonMaxWait()));

        return genericObjectPoolConfig;
    }
}
