package com.vasilev.news.config;

import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import com.vasilev.news.props.NetworkProperties;
import com.vasilev.news.props.SourcesProperties;

import lombok.RequiredArgsConstructor;
import lombok.val;

@Configuration
@RequiredArgsConstructor
public class NetworkConfig {

    private final SourcesProperties props;

    private final NetworkProperties networkProps;

    @Bean("rbkRestClient")
    public RestClient rbkRestClient(RestClient.Builder builder) {
        val rbkProps = props.getRbk();

        return builder
                .requestFactory(
                        createRequestFactory(
                                rbkProps.connectionTimeoutMillis(),
                                rbkProps.readTimeoutMillis())
                )
                .baseUrl(rbkProps.host())
                .build();
    }

    @Bean("common")
    public RestClient commonRestClient(RestClient.Builder builder) {
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
