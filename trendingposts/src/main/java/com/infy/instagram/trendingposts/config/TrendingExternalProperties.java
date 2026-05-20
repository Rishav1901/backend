package com.infy.instagram.trendingposts.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "external.post-service")
public class TrendingExternalProperties {
    private String baseUrl;
    private String postsAnalyticsPath;
    private String hashtagsAnalyticsPath;
}