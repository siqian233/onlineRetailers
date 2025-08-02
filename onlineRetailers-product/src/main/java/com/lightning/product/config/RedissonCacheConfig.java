package com.lightning.product.config;

import org.redisson.api.RedissonClient;
import org.redisson.spring.cache.CacheConfig;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Configuration
public class RedissonCacheConfig {
    @Bean
    public CacheManager cacheManager(RedissonClient redissonClient) {
        Map<String, CacheConfig> config = new HashMap<>();

        // 为"banners"缓存设置特定配置
        CacheConfig bannersCacheConfig = new CacheConfig(
                TimeUnit.HOURS.toMillis(24),     // 缓存生存时间24小时
                TimeUnit.MINUTES.toMillis(30)    // 最大空闲时间30分钟
        );
        config.put("banners", bannersCacheConfig);


        return new RedissonSpringCacheManager(redissonClient, config);
    }
}
