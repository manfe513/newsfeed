package com.vasilev.news.controller;

import com.vasilev.news.controller.HelloController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HelloController.class)
class HelloControllerTest {

    private static String STUB_NAME = "Joshua";
    private static String STUB_EXPECTED_JSON = "{\"name\":\"Joshua\"}";

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testHello() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/hello?name=%s".formatted(STUB_NAME)))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().json(STUB_EXPECTED_JSON)
                );
    }
}
