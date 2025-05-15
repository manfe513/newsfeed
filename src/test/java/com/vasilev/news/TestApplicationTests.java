package com.vasilev.news;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(initializers = TestContainersInitializer.class)
class TestApplicationTests {

	@Test
	void contextLoads() {
	}
}
