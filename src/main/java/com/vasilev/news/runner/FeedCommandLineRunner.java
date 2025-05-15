package com.vasilev.news.runner;

import com.vasilev.news.model.db.SourceEntity;
import com.vasilev.news.model.db.SourceRulesEntity;
import com.vasilev.news.repository.SourcesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
@Profile("dev")
public class FeedCommandLineRunner implements CommandLineRunner {

    private final SourcesRepository sourcesRepo;

    @Override
    public void run(String... args) {
        log.info("FeedCommandLineRunner: run..>");

        // ДЛЯ ПРОВЕРОК: изначально чтобы были пара источников
        sourcesRepo.saveAll(List.of(
                SourceEntity.builder()
                        .name("rbk")
                        .url("https://rssexport.rbc.ru/rbcnews/news/30/full.rss")
                        .rules(
                                SourceRulesEntity.builder()
                                        .itemPath("//item")
                                        .titlePath("title")
                                        .descriptionPath("description")
                                        .datePath("pubDate")
                                        .dateFormat("EEE, dd MMM yyyy HH:mm:ss Z")
                                        .build()
                        )
                        .build(),
                SourceEntity.builder()
                        .name("nytimes.com")
                        .url("https://rss.nytimes.com/services/xml/rss/nyt/World.xml")
                        .rules(
                                SourceRulesEntity.builder()
                                        .itemPath("//item")
                                        .titlePath("title")
                                        .descriptionPath("description")
                                        .datePath("pubDate")
                                        .dateFormat("EEE, dd MMM yyyy HH:mm:ss Z")
                                        .build()
                        )
                        .build()
        ));
    }
}
