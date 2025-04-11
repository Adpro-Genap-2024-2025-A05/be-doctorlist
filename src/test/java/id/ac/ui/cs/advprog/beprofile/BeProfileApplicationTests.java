package id.ac.ui.cs.advprog.beprofile;

import id.ac.ui.cs.advprog.beprofile.service.ProfileService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class BeProfileApplicationTests {

    @Autowired
    private ApplicationContext applicationContext;

    @MockBean
    private ProfileService profileService;

    @Test
    void contextLoads() {
        assertThat(applicationContext).isNotNull();
    }
}