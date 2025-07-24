package com.lightning.myPredicateFactory;

import lombok.Data;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.net.URI;

@Component
public class HelloLanguageRouteGatewayFilterFactory extends AbstractGatewayFilterFactory<HelloLanguageRouteGatewayFilterFactory.Config> {

    public HelloLanguageRouteGatewayFilterFactory() {
        super(Config.class);
    }

    @Data
    public static class Config {
        private String zhUri;
        private String enUri;
        private String jpUri;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String lang = exchange.getRequest().getHeaders().getFirst(HttpHeaders.ACCEPT_LANGUAGE);

            if(lang == null){
                return chain.filter(exchange);
            }

            String newPath;
            switch (lang.toLowerCase()) {
                case "zh" -> newPath = config.getZhUri(); // 这里存的是路径，如 /helloZh
                case "en" -> newPath = config.getEnUri();
                case "jp" -> newPath = config.getJpUri();
                default -> newPath = config.getZhUri();
            }

            URI newUri = URI.create(newPath);

            exchange.getAttributes().put(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR,
                    newUri);

            return chain.filter(exchange);
        };
    }
}

