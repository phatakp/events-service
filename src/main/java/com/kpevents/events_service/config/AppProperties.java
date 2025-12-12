package com.kpevents.events_service.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Configuration
@ConfigurationProperties(prefix = "app")
@Data
public class AppProperties {
    private String allowedOrigins;
    private String clerkJwksUrl;
    private String clerkIssuer;
    private String url;
}
