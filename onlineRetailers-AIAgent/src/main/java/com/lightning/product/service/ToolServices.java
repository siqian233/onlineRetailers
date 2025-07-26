package com.lightning.product.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;

@Service
public class ToolServices {

    @Tool("求一个字符串的长度")
    int stringLength(String s) {
        System.out.println("Called stringLength() with s='" + s + "'");
        return s.length();
    }

    @Tool("求任意多个数的和")
    int add(int... nums) {
        System.out.println("Called add() ****");
        int total = 0;
        for (int num : nums) {
            total += num;
        }
        System.out.println("和为:" + total);
        return total;
    }

    @Tool("计算一个数的平方根")
    double sqrt(int x) {
        double r = Math.sqrt(x);
        System.out.println("x=" + x + ",它的平方根为:" + r);
        return r;
    }

    @Tool("用户会询问某个城市的天气,他还会给你温度单位，请返回城市的天气情况")
    public String getWeather(
            @P("城市名称") String city,
            @P("温度单位") TemperatureUnit unit
    ) {
        try {
            String apiKey = System.getenv("WEATHER_API_KEY");
            String url = String.format(
                    "https://api.seniverse.com/v3/weather/now.json?key=%s&location=%s&language=zh-Hans&unit=c",
                    apiKey, URLEncoder.encode(city, StandardCharsets.UTF_8)
            );

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(Duration.ofSeconds(10))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String json = response.body();

            ObjectMapper mapper = new ObjectMapper();
            WeatherResponse weatherResponse = mapper.readValue(json, WeatherResponse.class);

            WeatherResponse.WeatherData.Now now = weatherResponse.results.get(0).now;
            return String.format("%s的天气：%s，气温 %s°C", city, now.text, now.temperature);
        } catch (Exception e) {
            e.printStackTrace();
            return "获取天气失败：" + e.getMessage();
        }
    }

    //温度单位的枚举类
    public enum TemperatureUnit {
        CELSIUS, FAHRENHEIT, KELVIN
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class WeatherResponse {
        @JsonProperty("results")
        public List<WeatherData> results;

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class WeatherData {
            @JsonProperty("location")
            @JsonIgnoreProperties(ignoreUnknown = true)
            public Location location;

            @JsonProperty("now")
            @JsonIgnoreProperties(ignoreUnknown = true)
            public Now now;

            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Location {
                @JsonProperty("name")
                public String name;
            }

            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Now {
                @JsonProperty("text")
                public String text;
                @JsonProperty("temperature")
                public String temperature;
                @JsonProperty("code")  // 新增code字段
                public String code;  // 虽然不使用但需要定义
            }
        }
    }


}
