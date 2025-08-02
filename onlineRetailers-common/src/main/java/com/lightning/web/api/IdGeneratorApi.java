package com.lightning.web.api;

import com.lightning.web.bean.ResponseResult;
import com.lightning.web.bean.TimeResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "idGenerator")
public interface IdGeneratorApi {

    /**
     * summary = "生成一个Id号", description = "生成一个Id号，格式:[ 1bit | 41bit时间戳 | 10bit机器ID（5 datacenter + 5 worker）| 12bit序列号 ]"
     *
     * @return ResponseResult
     */
    @GetMapping("/id/next")
    ResponseResult<Long> getNextId();

    /**
     * summary = "生成size个Id号", description = "Id号格式:[ 1bit | 41bit时间戳 | 10bit机器ID（5 datacenter + 5 worker）| 12bit序列号 ]"
     *
     * @param size 生成个数
     * @return ResponseResult
     */
    @GetMapping("/id/batch")
    ResponseResult<List<Long>> getBatchIds(@RequestParam(value = "size", defaultValue = "10") int size);

    /**
     * 解析时间戳
     */
    @GetMapping("/id/parse/time")
    ResponseResult<TimeResult> parseTime(@RequestParam("id") long id);

    /**
     * 解析 datacenterId
     */
    @GetMapping("/id/parse/datacenter")
    ResponseResult<Long> parseDatacenter(@RequestParam("id") long id);

    /**
     * 解析 workerId
     */
    @GetMapping("/id/parse/worker")
    ResponseResult<Long> parseWorker(@RequestParam("id") long id);


}

