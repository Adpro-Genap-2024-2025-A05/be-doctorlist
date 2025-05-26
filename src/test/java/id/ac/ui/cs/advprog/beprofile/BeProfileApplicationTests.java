package id.ac.ui.cs.advprog.beprofile;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {})  // Don't load any beans
@ContextConfiguration(classes = BeProfileApplicationTests.class)  // Only load test configuration
class BeProfileApplicationTests {
    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void contextLoads() {
        // Verify that the Spring application context loads successfully
        assertThat(applicationContext).isNotNull();
    }
}
