package com.vasilev.news.repository;

import com.vasilev.news.SourcesTestData;
import com.vasilev.news.TestContainersInitializer;
import lombok.val;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ContextConfiguration(initializers = TestContainersInitializer.class)
@ActiveProfiles("test")
class SourcesRepositoryTest {

    @Autowired
    SourcesRepository repo;

    @Test
    void shouldSaveFeedItems() {
        repo.deleteAll();

        val stubItem = SourcesTestData.getEntity();
        val savedItem = repo.save(stubItem);

        assertThat(savedItem)
                .isEqualTo(stubItem);
    }

    @AfterEach
    void afterEach() {
        repo.deleteAll();
    }
}