package com.lightning.myPredicateFactory;

import jakarta.validation.constraints.NotNull;
import org.springframework.cloud.gateway.handler.predicate.AbstractRoutePredicateFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

@Component
public class TimeLimitBetweenRoutePredicateFactory extends AbstractRoutePredicateFactory<TimeLimitBetweenRoutePredicateFactory.Config> {

    public static final String DATETIME1_KEY = "datetime1";
    public static final String DATETIME2_KEY = "datetime2";

    public TimeLimitBetweenRoutePredicateFactory() {
        super(Config.class);
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList(DATETIME1_KEY, DATETIME2_KEY);
    }

    @Override
    public Predicate<ServerWebExchange> apply(Config config) {
        LocalTime beginTime = config.getDatetime1();
        LocalTime endTime = config.getDatetime2();

        return new Predicate<ServerWebExchange>() {
            @Override
            public boolean test(ServerWebExchange serverWebExchange) {
                LocalTime now = LocalTime.now();
                return now.isAfter(beginTime) && now.isBefore(endTime);
            }
        };
    }

    public static class Config {
        private @NotNull LocalTime datetime1;
        private @NotNull LocalTime datetime2;

        public LocalTime getDatetime1() {
            return this.datetime1;
        }

        public Config setDatetime1(LocalTime datetime1) {
            this.datetime1 = datetime1;
            return this;
        }

        public LocalTime getDatetime2() {
            return this.datetime2;
        }

        public Config setDatetime2(LocalTime datetime2) {
            this.datetime2 = datetime2;
            return this;
        }
    }
}
