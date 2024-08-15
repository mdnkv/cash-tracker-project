package dev.mednikov.cashtracker.core;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@ActiveProfiles("test")
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BaseIT {

    protected final static Faker faker = new Faker();

    @Autowired
    protected TestRestTemplate testRestTemplate;

    private final static MariaDBContainer<?> container = new MariaDBContainer<>("mariadb:11.4")
            .withUsername("user")
            .withPassword("password")
            .withDatabaseName("test_db");

    @DynamicPropertySource
    static void setContainer (DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.password", container::getPassword);
        registry.add("spring.datasource.username", container::getUsername);
    }

    @BeforeAll
    static void runContainer() {
        container.start();
    }

}
