package com.vasilev.news.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.MappingBuilder;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.vasilev.news.TestContainersInitializer;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WireMockTest
@AutoConfigureMockMvc
@ContextConfiguration(initializers = TestContainersInitializer.class)
@ActiveProfiles("test")
abstract class AbstractControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @RegisterExtension
    static WireMockExtension wireMockExtension = WireMockExtension.newInstance()
            .options(WireMockConfiguration.wireMockConfig().dynamicPort())
            .build();

    protected void stubFor(MappingBuilder mappingBuilder) {
        wireMockExtension.stubFor(mappingBuilder);
    }

    @SneakyThrows
    protected void checkPost(String endpoint, Object content, ResultMatcher... matchers) {
        mockMvc.perform(
                        MockMvcRequestBuilders.post(endpoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(content))
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpectAll(matchers);
    }

    @SneakyThrows
    protected void checkGet(String url, @NonNull ResultMatcher... matchers) {
        mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andDo(print())
                .andExpectAll(matchers);
    }
}