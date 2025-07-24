package com.lightning.config;

import com.lightning.util.SnowflakeIdGenerator;
import com.lightning.util.WorkerIdAssigner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SnowflakeConfig {

    @Autowired
    private WorkerIdAssigner workerIdAssigner;

    @Bean
    public SnowflakeIdGenerator snowflakeIdGenerator() {
        long workerId = workerIdAssigner.getWorkerId();
        // 创建 SnowflakeIdGenerator 实例需要两个参数：workerId 和 datacenterId
        return new SnowflakeIdGenerator(workerId, 0  );  // datacenterId 可以根据实际情况设置
    }
}

