package com.todos.integration_tests.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for application properties.
 * Provides access to the TODO API URL.
 */
@Getter
@Configuration
public class AppProperties {
    @Value("${clientApi.url.todo}")
    private String todoUrl;
}
