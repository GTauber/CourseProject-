package com.ead.authuser.config;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.result.method.HandlerMethodArgumentResolver;
import org.springframework.web.reactive.result.method.annotation.ArgumentResolverConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebFluxConfigurer {

    private final List<HandlerMethodArgumentResolver> customMethodArgumentResolvers;

    @Override
    public void configureArgumentResolvers(ArgumentResolverConfigurer configurer) {
        customMethodArgumentResolvers.forEach(configurer::addCustomResolver);
    }

}
