package id.ac.ui.cs.advprog.beprofile;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class BeProfileApplicationTests {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void contextLoads() {
        // Verify that the Spring application context loads successfully
        assertThat(applicationContext).isNotNull();
    }

    @Test
    void testBeanLoading() {
        // Verify that a specific bean (e.g., UserService) is loaded in the context
        assertThat(applicationContext.containsBean("userService")).isTrue();
    }

    @Test
    void testApplicationProperties() {
        // Verify that application properties are loaded correctly
        String appName = applicationContext.getEnvironment().getProperty("spring.application.name");
        assertThat(appName).isEqualTo("be-profile"); // Replace "be-profile" with your actual application name
    }
}