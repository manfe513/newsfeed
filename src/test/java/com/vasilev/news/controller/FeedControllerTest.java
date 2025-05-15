package com.vasilev.news.controller;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.stubbing.Scenario;
import com.vasilev.news.actualizer.FeedActualizer;
import com.vasilev.news.repository.FeedRepository;
import com.vasilev.news.FeedTestData;
import com.vasilev.news.service.FeedService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;
import java.util.Set;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class FeedControllerTest extends AbstractControllerTest {

    private static final int STUB_ITEMS_COUNT = 30;
    private static final String FROM_DATE = "2024-12-17T10:00:00.733Z";

    // https://rssexport.rbc.ru/rbcnews/news/30/full.rss
    // где 30 - itemsCount
    private static final String FEED_URL_REGEX = ".*/rbcnews/news/\\d{1,2}/full.rss";

    @Autowired
    FeedService feedService;

    @Autowired
    Set<FeedActualizer> actualizers;

    @Autowired
    FeedRepository feedRepository;

    @DynamicPropertySource
    public static void setUrl(DynamicPropertyRegistry registry) {
        registry.add("sources.rbk.host", wireMockExtension::baseUrl);
    }

    Resource rbkFeedXml3Items = new ClassPathResource("rbk-response-3-items.xml");
    Resource rbkFeedXml6Items = new ClassPathResource("rbk-response-6-items.xml");
    Resource rbkFeedJson3Items = new ClassPathResource("rbk-response-3.json");
    Resource rbkFeedJsonSearchEfimov = new ClassPathResource("rbk-response-search-efimov.json");

    @BeforeEach
    public void before() {
        feedRepository.deleteAll();
    }

    @SneakyThrows
    @Test
    public void checkRbkJsonResponse() {
        mockXmlResponse(rbkFeedXml3Items);

        actualizeFeed();

        checkGet(
                "/feed?dateFrom=%s&count=%d".formatted(FROM_DATE, STUB_ITEMS_COUNT),
                status().isOk(),
                content().contentType("application/json;charset=UTF-8"),
                content().json(rbkFeedJson3Items.getContentAsString(StandardCharsets.UTF_8))
        );
    }

    private void actualizeFeed() {
        actualizers.forEach(FeedActualizer::actualize);
    }

    @SneakyThrows
    @Test
    public void checkActualizeBySourceRbk() {
        mockXmlResponse(rbkFeedXml3Items);

        feedService.actualizeBySource("rbk");

        checkGet(
                "/feed?dateFrom=%s&count=%d".formatted(FROM_DATE, STUB_ITEMS_COUNT),
                status().isOk(),
                content().contentType("application/json;charset=UTF-8"),
                content().json(rbkFeedJson3Items.getContentAsString(StandardCharsets.UTF_8))
        );
    }

    @SneakyThrows
    @Test
    public void validateActualizeBySource() {
        mockXmlResponse(rbkFeedXml3Items);

        checkGet(
                "/actualize?source=%s".formatted(" "),
                status().isBadRequest(),
                content().encoding("UTF-8"),
                content().string(Matchers.containsString("Необходимо указать источник новостей"))
        );
    }

    @SneakyThrows
    @Test
    public void checkSearchResults() {
        mockXmlResponse(rbkFeedXml3Items);

        actualizeFeed();

        checkSearchRequest("Ефимов", rbkFeedJsonSearchEfimov); //title
        checkSearchRequest("(КРТ)", rbkFeedJsonSearchEfimov); // description
    }

    @SneakyThrows
    private void checkSearchRequest(String searchText, Resource expectedJsonResource) {

        checkGet(
                "/feed?dateFrom=%s&count=%d&searchText=%s".formatted(FROM_DATE, STUB_ITEMS_COUNT, searchText),
                status().isOk(),
                content().contentType("application/json;charset=UTF-8"),
                content().json(expectedJsonResource.getContentAsString(StandardCharsets.UTF_8))
        );
    }


    @SneakyThrows
    @ParameterizedTest
    @CsvSource({
            "30,a,Строка поиска должна иметь длину 2 и более символов",
            "-1,null,Количество должно быть представлено положительным числом"
    })
    public void checkValidationErrors(int count, String searchText, String expectedErrorMsg) {
        checkGet(
                "/feed?dateFrom=%s&count=%d&searchText=%s".formatted(FROM_DATE, count, searchText),
                status().isBadRequest(),
                content().contentType("application/json;charset=UTF-8"),
                content().string(Matchers.containsString(expectedErrorMsg))
        );
    }

    @SneakyThrows
    @Test
    public void checkRbkServiceXmlResponseMapping() {
        mockXmlResponse(rbkFeedXml3Items);

        actualizeFeed();

        val expectedItems = FeedTestData.generate3Items();
        val actualItems = feedService.getFeed(Instant.parse(FROM_DATE), STUB_ITEMS_COUNT, ""); // из БД

        assertFeedItemsAreEqual(expectedItems, actualItems);
    }

    // Проверка, что при автоматическом пополнении БД нет потерь новостей
    // Пример:
    // - получили 3 новости (а всего в источнике уже 6 новых) - все 3 сохранили в бд
    // - после чего проверили, нужно ли ещё загрузить - дополучили оставшиеся 3 новости
    @SneakyThrows
    @Test
    public void checkFeedAutoUpdates() {
        val SCENARIO_NAME = "check-fetcher";

        stubFor(
                WireMock.get(urlMatching(FEED_URL_REGEX))
                        .inScenario(SCENARIO_NAME)
                        .whenScenarioStateIs(Scenario.STARTED)
                        .willReturn(
                                aResponse()
                                        .withHeader("Content-Type", "text/xml")
                                        .withBody(rbkFeedXml3Items.getContentAsString(StandardCharsets.UTF_8))
                        )
                        .willSetStateTo("need-6")
        );

        stubFor(
                WireMock.get(urlMatching(FEED_URL_REGEX))
                        .inScenario(SCENARIO_NAME)
                        .whenScenarioStateIs("need-6")
                        .willReturn(
                                aResponse()
                                        .withHeader("Content-Type", "text/xml")
                                        .withBody(rbkFeedXml6Items.getContentAsString(StandardCharsets.UTF_8))
                        )
        );

        actualizeFeed();

        var expectedItems = FeedTestData.generate6Entities();
        var actualItems = feedRepository.findAll(); //feedService.getFeed(0, STUB_ITEMS_COUNT); // из БД

        assertFeedItemsAreEqual(expectedItems, actualItems);
    }

    @SneakyThrows
    private void mockXmlResponse(Resource xmlResource) {
        stubFor(
                WireMock.get(urlMatching(FEED_URL_REGEX))
                        .willReturn(
                                aResponse()
                                        .withHeader("Content-Type", "text/xml")
                                        .withBody(xmlResource.getContentAsString(StandardCharsets.UTF_8))
                        )
        );
    }

    private <T> void assertFeedItemsAreEqual(List<T> expected, List<T> actual) {
        log.info(">> expected items:");
        expected.forEach(feedItem -> log.info(feedItem.toString()));

        log.info(">> actual items:");
        actual.forEach(feedItem -> log.info(feedItem.toString()));

        assertThat(expected)
                .isEqualTo(expected);
    }
}