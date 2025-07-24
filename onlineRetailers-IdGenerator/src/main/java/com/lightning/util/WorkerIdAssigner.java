package com.lightning.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.util.List;

/**
 * @program: cloud147
 * @description:  通过注册中心Nacos中实例数量 → 实例 ID → 生成 workerId
 * @author: zy
 * @create: 2025-07-10 19:13
 */
@Component
public class WorkerIdAssigner {

    @Autowired  //利用DI注入     nacos客户端, 用于获取服务实例信息
    private DiscoveryClient discoveryClient;
    // 注入 Nacos 注册中心服务

    @Value("${spring.application.name}")  //注入服务名称, 用于获取服务实例信息  -》 workerId
    private String serviceName;

    public long getWorkerId() {
        try {
            //根据服务名称获取实例列表
            List<ServiceInstance> instances = discoveryClient.getInstances(serviceName);
            //获取当前实例的IP地址
            String currentIp = InetAddress.getLocalHost().getHostAddress();

            int index = 0;
            for (int i = 0; i < instances.size(); i++) {
                if (instances.get(i).getHost().equals(currentIp)) {
                    index = i;
                    break;
                }
            }
            return index;   //此处返回的是实例的索引, 作为workerId
        } catch (Exception e) {
            throw new RuntimeException("Worker ID 获取失败", e);
        }
    }
}
