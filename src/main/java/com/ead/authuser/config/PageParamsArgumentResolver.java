package com.ead.authuser.config;

import static com.ead.authuser.utils.MethodArgumentResolverUtils.getQueryParamsMap;

import java.util.Map;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.lang.NonNull;
import org.springframework.web.reactive.BindingContext;
import org.springframework.web.reactive.result.method.HandlerMethodArgumentResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Configuration
public class PageParamsArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return Pageable.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    @NonNull
    public Mono<Object> resolveArgument(@NonNull MethodParameter parameter, @NonNull BindingContext bindingContext,
        @NonNull ServerWebExchange exchange) {
        var queryParams = getQueryParamsMap(exchange);
        var page = resolvePageParams(queryParams);
        return Mono.just(page);
    }

    private Pageable resolvePageParams(Map<String, String> queryParams) {
        return PageRequest.of(Integer.parseInt(queryParams.getOrDefault("page", "0")), Integer.parseInt(
                queryParams.getOrDefault("size", "0")),
            getSortFromParams(queryParams));
    }

    private Sort getSortFromParams(Map<String, String> queryParams) {
        var dir = Direction.fromOptionalString(queryParams.get("sortOrder")).orElse(Direction.ASC);
        return Sort.by(dir, queryParams.getOrDefault("sort", "createdAt"));
    }
}
