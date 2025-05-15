package com.vasilev.news.repository;

import com.vasilev.news.FeedTestData;
import com.vasilev.news.TestContainersInitializer;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
@ContextConfiguration(initializers = TestContainersInitializer.class)
@ActiveProfiles("test")
class FeedRepositoryTest {

    @Autowired
    FeedRepository feedRepository;

    @Test
    void shouldSaveFeedItems() {
        feedRepository.deleteAll();

        val existingItems = feedRepository.findAll();
        existingItems.forEach(item -> log.info(item.toString()));

        val stubItems = FeedTestData.generate3Entities();
        val savedItems = feedRepository.saveAll(stubItems);


        assertThat(savedItems)
                .isEqualTo(stubItems);
    }
}