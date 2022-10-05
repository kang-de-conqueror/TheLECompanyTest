package co.lecompany.app.config.redis;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spring.redis")
@Getter
public class RedisCacheConfigProperties {

    @Value("${spring.redis.common.host}")
    private String commonHost;

    @Value("${spring.redis.common.port}")
    private Integer commonPort;

    @Value("${spring.redis.common.password}")
    private String commonPassword;

    @Value("${spring.redis.timeout}")
    private Integer timeout;

    @Value("${spring.redis.database}")
    private Integer database;

    @Value("${spring.redis.common.maxIdle}")
    private Integer commonMaxIdle;

    @Value("${spring.redis.common.minIdle}")
    private Integer commonMinIdle;

    @Value("${spring.redis.common.maxActive}")
    private Integer commonMaxActive;

    @Value("${spring.redis.common.maxWait}")
    private Integer commonMaxWait;
}