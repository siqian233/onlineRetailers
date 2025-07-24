package com.lightning.web.controller;

import com.lightning.bean.ResponseResult;
import com.lightning.util.SnowflakeIdGenerator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.java.Log;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/id")
@Log
@Tag(name = "IdController", description = "分布式的Id生成器")
public class IdGeneratorController {

    private final SnowflakeIdGenerator generator = new SnowflakeIdGenerator(1, 1);

    /**
     * 单个 ID
     */
    @RequestMapping("/next")
    @Operation(summary = "生成一个Id号", description = "生成一个Id号，格式:[ 1bit | 41bit时间戳 | 10bit机器ID（5 datacenter + 5 worker）| 12bit序列号 ]")
    public ResponseResult getNextId() {
        long id=generator.nextId();
        log.info("调用nextId()生成的id号为:" +  id );   //日志: 异步记录
        return ResponseResult.ok().setData(id);
    }

    /**
     * 批量 ID
     */
    @GetMapping("/batch")
    @Operation(summary = "生成size个Id号", description = "Id号格式:[ 1bit | 41bit时间戳 | 10bit机器ID（5 datacenter + 5 worker）| 12bit序列号 ]")
    public ResponseResult getBatchIds(@RequestParam(value="size", defaultValue = "10") int size) {
        List<Long> ids = generator.nextIdBatch(size);
        return ResponseResult.ok().setData(ids);
    }

    /**
     * 解析时间戳
     */
    @GetMapping("/parse/time")
    @Operation(summary = "根据Id号解析出时间戳", description = "格式: \"timestamp\": 1752283999462,\n" +
            "        \"date\": \"2025-07-12T01:33:19.462+00:00\"")
    public ResponseResult parseTime(@RequestParam("id") long id) {
        long ts = generator.parseIdToTime(id);
        return ResponseResult.ok().setData(new TimeResult(ts, new Date(ts)));
    }

    /**
     * 解析 datacenterId
     */
    @GetMapping("/parse/datacenter")
    @Operation(summary = "根据Id号解析数据中心号")
    public ResponseResult parseDatacenter(@RequestParam("id") long id) {
        return ResponseResult.ok().setData(generator.parseIdToDatacenterId(id));
    }

    /**
     * 解析 workerId
     */
    @GetMapping("/parse/worker")
    @Operation(summary = "根据Id号解析机器号")
    public ResponseResult parseWorker(@RequestParam("id") long id) {
        return ResponseResult.ok().setData(generator.parseIdToWorkerId(id));
    }

    @GetMapping("/fallback/next")//限流友好服务
    public ResponseResult fallback() {
        return ResponseResult.error("节假日访问高峰，服务已限流");
    }

    /**
     * 内部时间封装类
     */
    @lombok.Data
    @lombok.AllArgsConstructor
    static class TimeResult {
        private long timestamp;
        private Date date;
    }




}
