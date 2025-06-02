package com.vasilev.news.props;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "sources")
public class SourcesProperties {

    @NestedConfigurationProperty
    private final FeedSourceProperties rbk;

    public record FeedSourceProperties(
            String host,
            int connectionTimeoutMillis,
            int readTimeoutMillis,
            boolean actualizerEnabled,
            int actualizerPeriodMillis,
            String actualizerCron,

            int pageSize
    ) {}
}