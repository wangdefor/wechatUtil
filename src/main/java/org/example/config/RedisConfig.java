package org.example.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.spring.data.connection.RedissonConnectionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @ClassName RedisKeyConfig
 * @Description 活动配置redis
 * @Author yu ming sheng
 * @Date 2019/10/10 20:06
 * @Version
 */
@Configuration
public class RedisConfig {

    /**
     * 活动相关redis配置名
     */
    public static final String DEFAULT_REDIS_NAME = "default_redis_name";

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private String port;

    @Value("${spring.redis.password}")
    private String password;

    @Bean(DEFAULT_REDIS_NAME)
    public RedissonConnectionFactory redissonConnectionFactory(@Qualifier("redissonClient") RedissonClient redissonClient){
        return new RedissonConnectionFactory(redissonClient);
    }

    @Bean(destroyMethod = "shutdown", name = "redissonClient")
    public RedissonClient redisson(){
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://" + host + ":" + port)
                .setPassword(password);
        return Redisson.create(config);
    }


    @Bean(name = DEFAULT_REDIS_NAME)
    public StringRedisTemplate redisOrderTemplate() {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(redissonConnectionFactory(redisson()));
        template.afterPropertiesSet();
        return template;
    }

}
