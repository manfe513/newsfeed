package com.vasilev.news;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.support.GenericApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;

public class TestContainersInitializer implements ApplicationContextInitializer<GenericApplicationContext> {

    public static final PostgreSQLContainer<?> postgreSQLContainer;

    static {
        postgreSQLContainer = new PostgreSQLContainer<>("postgres:17-alpine");
        postgreSQLContainer.start();
    }

    @Override
    public void initialize(GenericApplicationContext applicationContext) {
        TestPropertyValues.of(
                "spring.datasource.url=" + postgreSQLContainer.getJdbcUrl(),
                "spring.datasource.username=" + postgreSQLContainer.getUsername(),
                "spring.datasource.password=" + postgreSQLContainer.getPassword(),

                "spring.flyway.url=" + postgreSQLContainer.getJdbcUrl(),
                "spring.flyway.user=" + postgreSQLContainer.getUsername(),
                "spring.flyway.password=" + postgreSQLContainer.getPassword()
        ).applyTo(applicationContext.getEnvironment());
    }
}
