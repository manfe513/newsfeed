package com.vasilev.news.config;

import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import com.vasilev.news.props.NetworkProperties;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class NetworkConfig {

    private final NetworkProperties networkProps;

    @Bean
    public RestClient restClient(RestClient.Builder builder) {
        return builder
                .requestFactory(
                        createRequestFactory(
                                networkProps.connectionTimeoutMillis,
                                networkProps.readTimeoutMillis
                        )
                )
                .build();
    }


    private SimpleClientHttpRequestFactory createRequestFactory(
            int connTimeout,
            int readTimeout
    ) {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofSeconds(connTimeout));
        factory.setReadTimeout(Duration.ofSeconds(readTimeout));

        return factory;
    }
}
