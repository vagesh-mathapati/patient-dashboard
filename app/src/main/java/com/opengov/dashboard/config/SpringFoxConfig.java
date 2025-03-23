package com.opengov.dashboard.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringFoxConfig {
    @Bean
    public GroupedOpenApi customOpenAPI() {
        return GroupedOpenApi.builder()
                .group("v1")  // API Group Name
                .addOpenApiCustomizer(openApi -> openApi.setServers(null))  // Optional customization
                .pathsToMatch("/**")  // Specify which paths to include
                .build();
    }
}