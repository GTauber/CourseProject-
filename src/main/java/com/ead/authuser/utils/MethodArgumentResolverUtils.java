package com.ead.authuser.utils;

import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.web.server.ServerWebExchange;

public class MethodArgumentResolverUtils {

    private MethodArgumentResolverUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static Map<String, String> getQueryParamsMap(ServerWebExchange exchange) {
        return exchange.getRequest().getQueryParams()
            .toSingleValueMap()
            .entrySet()
            .stream()
            .filter(entry -> entry.getValue() != null && !entry.getValue().isBlank())
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue
            ));
    }

}
