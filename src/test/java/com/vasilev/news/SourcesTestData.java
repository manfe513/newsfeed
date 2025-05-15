package com.vasilev.news;

import com.vasilev.news.model.domain.Source;
import com.vasilev.news.model.domain.SourceRules;
import com.vasilev.news.model.db.SourceEntity;
import com.vasilev.news.model.db.SourceRulesEntity;

public class SourcesTestData {

    public static SourceEntity getEntity() {
        return SourceEntity.builder()
                .name("rbk")
                .url("https://rssexport.rbc.ru/rbcnews/news/90/full.rss")
                .rules(
                        SourceRulesEntity.builder()
                                .itemPath("//item")
                                .titlePath("title")
                                .descriptionPath("description")
                                .datePath("pubDate")
                                .dateFormat("EEE, dd MMM yyyy HH:mm:ss Z")
                                .build()
                )
                .build();
    }

    public static Source getSource() {
        return Source.builder()
                .name("rbk")
                .url("https://rssexport.rbc.ru/rbcnews/news/90/full.rss")
                .rules(
                        SourceRules.builder()
                                .itemPath("//item")
                                .titlePath("title")
                                .descriptionPath("description")
                                .datePath("pubDate")
                                .dateFormat("EEE, dd MMM yyyy HH:mm:ss Z")
                                .build()
                )
                .build();
    }
}
