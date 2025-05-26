package id.ac.ui.cs.advprog.beprofile.controller;

import id.ac.ui.cs.advprog.beprofile.dto.ApiResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class HomeControllerTest {

    private HomeController homeController;

    @BeforeEach
    void setUp() {
        homeController = new HomeController();
    }

    @Test
    void healthCheck_shouldReturnUpStatus() {
        ResponseEntity<ApiResponseDto<Map<String, String>>> response = homeController.healthCheck();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        ApiResponseDto<Map<String, String>> body = response.getBody();
        assertNotNull(body);
        assertEquals(200, body.getStatus());
        assertEquals("Service is up and running", body.getMessage());

        Map<String, String> data = body.getData();
        assertNotNull(data);
        assertEquals("UP", data.get("status"));
        assertEquals("Back-End Doctor List API", data.get("service"));
    }

    @Test
    void healthCheck_shouldIncludeTimestamp() {
        ResponseEntity<ApiResponseDto<Map<String, String>>> response = homeController.healthCheck();

        ApiResponseDto<Map<String, String>> body = response.getBody();
        assertNotNull(body);
        assertNotNull(body.getTimestamp());
    }
}