package com.lightning.web.bean;

import lombok.Data;

@Data
public class WeatherBean {
    private String city;         // 城市名称
    private String date;         // 日期，例如“2025-06-30”
    private String weather;      // 天气状况，例如“多云转晴”
    private String temperature;  // 温度范围，例如“24℃ / 32℃”
    private String humidity;     // 湿度，例如“58%”
    private String wind;         // 风力，例如“西南风 3 级”
    private String advice;       // 建议内容，例如“外出注意防晒”
}
