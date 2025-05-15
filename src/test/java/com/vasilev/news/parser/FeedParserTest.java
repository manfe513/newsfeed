package com.vasilev.news.parser;

import com.vasilev.news.FeedTestData;
import com.vasilev.news.TestContainersInitializer;
import com.vasilev.news.model.db.FeedEntity;
import com.vasilev.news.model.db.SourceRulesEntity;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@Slf4j
@SpringBootTest
@ContextConfiguration(initializers = TestContainersInitializer.class)
@ActiveProfiles("test")
class FeedParserTest {

    @Autowired
    FeedParser parser;

    Resource rbkFeedXml3Items = new ClassPathResource("rbk-response-3-items.xml");

    @SneakyThrows
    @Test
    void parse() {
        List<FeedEntity> feedEntities = parser.parse(
                "rbk",
                rbkFeedXml3Items.getContentAsString(StandardCharsets.UTF_8),
                SourceRulesEntity.builder()
                        .id(1)
                        .itemPath("//item")
                        .titlePath("title")
                        .descriptionPath("description")
                        .datePath("pubDate")
                        .dateFormat("EEE, dd MMM yyyy HH:mm:ss Z")
                        .build()
        );
        feedEntities.forEach(e -> log.info("parsed entity in test: {}", e));


        assertThat(feedEntities)
                .isEqualTo(FeedTestData.generate3Entities());
    }
}