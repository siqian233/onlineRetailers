package com.lightning.util;

import cn.hutool.core.lang.Snowflake;

import java.util.ArrayList;
import java.util.List;

public class SnowflakeIdGenerator {

    private final long workerId;
    private final long datacenterId;
    private final long sequence = 0L;
    private final int batchMaxSize = 1000;

    private final Snowflake snowflake;

    public SnowflakeIdGenerator(long workerId, long datacenterId) {
        this.workerId = workerId;
        this.datacenterId = datacenterId;

        this.snowflake = new Snowflake(workerId, datacenterId);
    }

    public synchronized long nextId() {
        return snowflake.nextId();
    }

    /**
     * 批量生成ID
     * 最大1000
     * @param size
     * @return
     */
    public  List<Long> nextIdBatch(int size) {
        if (size > batchMaxSize) {
            size = batchMaxSize;
        }
        List<Long> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(  nextId()  );
        }
        return list;
    }

    /**
     *  解析时间戳
     */
    public long parseIdToTime(long id) {
        return snowflake.getGenerateDateTime( id );
    }

    /**
     *  解析数据中心ID
     */
    public long parseIdToDatacenterId(long id) {
        return snowflake.getDataCenterId(id );
    }

    /**
     *  解析workerId
     */
    public long parseIdToWorkerId(long id) {
        return snowflake.getWorkerId( id );
    }
}

