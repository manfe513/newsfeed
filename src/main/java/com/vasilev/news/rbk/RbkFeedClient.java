package com.vasilev.news.rbk;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.vasilev.news.rbk.model.RbkFeed;
import com.vasilev.news.rbk.model.RbkFeedItem;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class RbkFeedClient {

    private static final String FEED_ENDPOINT = "/rbcnews/news/%d/full.rss";
    private static final int MAX_PAGE_SIZE = 90;

    @Qualifier("rbkRestClient")
    private final RestClient restClient;

    public List<RbkFeedItem> getFeed(int itemsCount) {
        if (itemsCount > MAX_PAGE_SIZE) {
            throw new IllegalArgumentException("RBK: invalid pageSize = %d".formatted(itemsCount));
        }

        RbkFeed response = getRbkFeedResponse(itemsCount);

        log.info("RBK FEED ITEMS:");
        response.getItems().forEach(item -> log.info(item.toString()));

        return response.getItems() != null
                ? response.getItems()
                : List.of();
    }

    private RbkFeed getRbkFeedResponse(int itemsCount) {
        String url = FEED_ENDPOINT.formatted(itemsCount);

        return restClient
                .get()
                .uri(url)
                .accept(MediaType.TEXT_XML)
                .retrieve()
                .toEntity(RbkFeed.class)
                .getBody();
    }
}
