package id.ac.ui.cs.advprog.beprofile.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class AppConfigTest {

    @Autowired
    private AppConfig AppConfig;

    @Test
    public void testRestTemplateCreation() {
        RestTemplate restTemplate = AppConfig.restTemplate();
        assertNotNull(restTemplate);
    }

    @Test
    public void testRestTemplateAutowiring() {
        assertNotNull(AppConfig);
    }
}