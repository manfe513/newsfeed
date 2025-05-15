package com.vasilev.news.model.domain;

public record FeedListItem(
        int id,
        String date,
        String source,
        String title,
        String description
) {}
