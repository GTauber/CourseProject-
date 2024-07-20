package com.ead.authuser.config;

import static com.ead.authuser.utils.MethodArgumentResolverUtils.getQueryParamsMap;
import static com.ead.authuser.utils.MethodUtils.getSetterMethod;

import com.ead.authuser.models.query.Filterable;
import com.ead.authuser.models.query.annotation.FilterableField;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.lang.NonNull;
import org.springframework.web.reactive.BindingContext;
import org.springframework.web.reactive.result.method.HandlerMethodArgumentResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Configuration
public class FilterableParamsArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return Filterable.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    @NonNull
    public Mono<Object> resolveArgument(MethodParameter parameter, @NonNull BindingContext bindingContext,
        @NonNull ServerWebExchange exchange) {
        var clazz = parameter.getParameterType();
        try {
            var filterableInstance = clazz.getDeclaredConstructor().newInstance();
            var queryParams = getQueryParamsMap(exchange);
            setFilterableFieldValues(clazz, queryParams, filterableInstance);

            return Mono.just(filterableInstance);
        } catch (Exception e) {
            return Mono.error(e);
        }
    }

    private void setFilterableFieldValues(Class<?> clazz, Map<String, String> queryParams, Object instance)
        throws IllegalAccessException, InvocationTargetException {
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(FilterableField.class)) {
                String fieldName = field.getName();
                if (queryParams.containsKey(fieldName)) {
                    getSetterMethod(field).invoke(instance, queryParams.get(fieldName));
                }
            }
        }
    }
}
