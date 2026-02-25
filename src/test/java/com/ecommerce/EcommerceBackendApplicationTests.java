package com.ecommerce;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class EcommerceBackendApplicationTests {

    @Test
    void contextLoads() {
        // Test that the application context loads successfully
    }
}