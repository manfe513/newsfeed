package com.vasilev.news.props;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "network")
public class NetworkProperties {

    public int connectionTimeoutMillis;
    public int readTimeoutMillis;
}