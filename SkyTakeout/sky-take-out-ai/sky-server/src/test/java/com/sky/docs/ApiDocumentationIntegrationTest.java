package com.sky.docs;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApiDocumentationIntegrationTest {
    @LocalServerPort
    private int port;

    private final TestRestTemplate restTemplate = new TestRestTemplate();

    @Test
    void knife4jDocPageIsReachable() {
        ResponseEntity<String> response = restTemplate.getForEntity(url("/doc.html"), String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() != null && response.getBody().contains("Knife4j"));
    }

    @Test
    void openApiContainsEmployeeLoginEndpoint() {
        ResponseEntity<String> response = restTemplate.getForEntity(url("/v3/api-docs"), String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() != null && response.getBody().contains("/admin/employee/login"));
    }

    private String url(String path) {
        return "http://localhost:" + port + path;
    }
}
