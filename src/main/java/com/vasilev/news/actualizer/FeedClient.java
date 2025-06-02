package com.vasilev.news.actualizer;

import com.vasilev.news.model.db.SourceEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class FeedClient {

    private final RestClient restClient;

    public Optional<String> getFeed(SourceEntity source) {
        ResponseEntity<String> response = getFeedResponse(source.getUrl());

        return response.getStatusCode() == HttpStatus.OK
                ? Optional.ofNullable(response.getBody())
                : Optional.empty();
    }

    private ResponseEntity<String> getFeedResponse(String url) {

        return restClient
                .get()
                .uri(url)
                .accept(MediaType.TEXT_XML)
                .retrieve()
                .toEntity(String.class);
    }
}
