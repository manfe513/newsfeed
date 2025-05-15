package com.vasilev.news.controller;

import com.vasilev.news.SourcesTestData;
import com.vasilev.news.model.request.AddSourceRequest;
import com.vasilev.news.model.request.AddSourceRequestRules;
import com.vasilev.news.model.db.SourceEntity;
import com.vasilev.news.model.db.SourceRulesEntity;
import com.vasilev.news.model.request.RemoveSourceRequest;
import com.vasilev.news.repository.SourcesRepository;
import com.vasilev.news.service.SourcesService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ActiveProfiles;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SourcesControllerTest extends AbstractControllerTest {

    @Autowired
    SourcesService sourcesService;

    @Autowired
    SourcesRepository sourcesRepo;

    Resource getSourcesResponseJson = new ClassPathResource("get-sources-response.json");

    @BeforeEach
    public void beforeEach() {
        sourcesRepo.deleteAll();
    }

    @SneakyThrows
    @Test
    public void checkAddSource() {
        SourceEntity stubSource = SourcesTestData.getEntity();

        performAddSourceRequest(stubSource);

        SourceEntity dbSource = sourcesRepo.findAll().get(0);

        assertEquals(stubSource, dbSource);
    }

    private void performAddSourceRequest(SourceEntity stubSource) {
        AddSourceRequest request = createRequest(stubSource);
        checkPost(
                "/sources",
                request,
                status().isOk()
        );
    }

    @SneakyThrows
    @Test
    public void checkRemoveSource() {
        SourceEntity stubSource = SourcesTestData.getEntity();

        // проверили что в бд сохранился
        sourcesRepo.save(stubSource);
        SourceEntity dbSource = sourcesRepo.findAll().get(0);
        assertEquals(stubSource, dbSource);

        // проверка удаления через http api
        checkPost(
                "/sources/remove",
                new RemoveSourceRequest(dbSource.getId()),
                status().isOk()
        );
        assertEquals(0, sourcesRepo.count());
    }

    @SneakyThrows
    @Test
    public void checkFindAll() {
        checkPost(
                "/sources",
                createRequest(SourcesTestData.getEntity()),
                status().isOk()
        );

        checkGet(
                "/sources",
                status().isOk(),
                content().contentType("application/json;charset=UTF-8"),
                content().json(getSourcesResponseJson.getContentAsString(StandardCharsets.UTF_8))
        );
    }

    private AddSourceRequest createRequest(SourceEntity stubSource) {
        SourceRulesEntity stubSourceRules = stubSource.getRules();

        return AddSourceRequest.builder()
                .name(stubSource.getName())
                .url(stubSource.getUrl())
                .rules(
                        AddSourceRequestRules.builder()
                                .itemPath(stubSourceRules.getItemPath())
                                .titlePath(stubSourceRules.getTitlePath())
                                .descriptionPath(stubSourceRules.getDescriptionPath())
                                .datePath(stubSourceRules.getDatePath())
                                .dateFormat(stubSourceRules.getDateFormat())
                                .build()
                )
                .build();
    }
}