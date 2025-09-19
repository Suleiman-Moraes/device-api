package com.moraes.device_api.config;

import java.util.Map;
import java.util.stream.Stream;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.lifecycle.Startables;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor
@ContextConfiguration(initializers = AbstractIntegrationTest.Initializer.class)
public abstract class AbstractIntegrationTest {

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        static PostgreSQLContainer<?> postgresql = new PostgreSQLContainer<>("postgres:17.4-bookworm");

        private static void startContainers() {
            Startables.deepStart(Stream.of(postgresql)).join();
        }

        private static Map<String, String> createConnectionConfiguration() {
            return Map.of(
                    "spring.datasource.url", postgresql.getJdbcUrl() + "&currentSchema=device_api",
                    "spring.datasource.username", postgresql.getUsername(),
                    "spring.datasource.password", postgresql.getPassword());
        }

        @SuppressWarnings({ "unchecked", "rawtypes" })
        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            try {
                startContainers();
                ConfigurableEnvironment environment = applicationContext.getEnvironment();
                MapPropertySource testcontainers = new MapPropertySource("testcontainers",
                        (Map) createConnectionConfiguration());
                environment.getPropertySources().addFirst(testcontainers);
            } catch (Exception e) {
                log.error("Error to start containers", e);
            }
        }
    }
}
