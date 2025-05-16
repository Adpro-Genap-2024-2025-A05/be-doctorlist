package id.ac.ui.cs.advprog.beprofile.controller;

import id.ac.ui.cs.advprog.beprofile.model.Doctor;
import id.ac.ui.cs.advprog.beprofile.repository.DoctorRepository;
import id.ac.ui.cs.advprog.beprofile.repository.InMemoryDoctorRepository;
import id.ac.ui.cs.advprog.beprofile.service.DoctorSearchService;
import id.ac.ui.cs.advprog.beprofile.service.strategy.SearchByNameStrategy;
import id.ac.ui.cs.advprog.beprofile.service.strategy.SearchStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DoctorController.class)
public class DoctorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DoctorSearchService doctorSearchService;

    @TestConfiguration
    static class TestConfig {
        @Bean
        @Primary
        public DoctorRepository doctorRepository() {
            return new InMemoryDoctorRepository();
        }

        @Bean
        public SearchStrategy searchByNameStrategy() {
            return new SearchByNameStrategy();
        }

        @Bean
        public Map<String, SearchStrategy> strategies(SearchStrategy searchByNameStrategy) {
            Map<String, SearchStrategy> m = new HashMap<>();
            m.put("name", searchByNameStrategy);
            return m;
        }

        @Bean
        public DoctorSearchService doctorSearchService(
                DoctorRepository repo,
                Map<String, SearchStrategy> strategies
        ) {
            return new DoctorSearchService(repo, strategies);
        }
    }

    @BeforeEach
    void setUp() {
        doctorSearchService.clearDoctors();
        Doctor d = new Doctor();
        d.setId("doctor-123");
        d.setName("Dr. Bambang");
        d.setPracticeAddress("Jalan Bekasi Raya");
        d.setWorkSchedule("Mon-Fri 09:00-17:00");
        d.setEmail("dr.bambang@example.com");
        d.setPhoneNumber("081234567890");
        d.setRating(4.5);
        doctorSearchService.addDoctor(d);
    }

    @Test
    void testGetDoctorsByName() throws Exception {
        mockMvc.perform(get("/api/doctors")
                        .param("searchType", "name")
                        .param("criteria", "Bambang")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Dr. Bambang")));
    }

    @Test
    void testGetDoctorById() throws Exception {
        mockMvc.perform(get("/api/doctors/doctor-123")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Dr. Bambang")))
                .andExpect(jsonPath("$.email", is("dr.bambang@example.com")));
    }
}
