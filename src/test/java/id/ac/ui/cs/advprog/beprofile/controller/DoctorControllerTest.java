package id.ac.ui.cs.advprog.beprofile.controller;

import id.ac.ui.cs.advprog.beprofile.model.Doctor;
import id.ac.ui.cs.advprog.beprofile.service.DoctorSearchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

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
        public DoctorSearchService doctorSearchService() {
            return new DoctorSearchService();
        }
    }

    @BeforeEach
    void setUp() {
        doctorSearchService.clearDoctors();
        Doctor doctor = new Doctor();
        doctor.setId("doctor-123");
        doctor.setName("Dr. Bambang");
        doctor.setPracticeAddress("Jalan Bekasi Raya");
        doctor.setWorkSchedule("Mon-Fri 09:00-17:00");
        doctor.setEmail("dr.bambang@example.com");
        doctor.setPhoneNumber("081234567890");
        doctor.setRating(4.5);
        doctorSearchService.addDoctor(doctor);
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void testGetDoctorById() throws Exception {
        mockMvc.perform(get("/doctors/doctor-123")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is("Dr. Bambang")))
                .andExpect(jsonPath("$.email", is("dr.bambang@example.com")));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void testGetDoctorsByName() throws Exception {
        mockMvc.perform(get("/doctors")
                .param("searchType", "name")
                .param("criteria", "Bambang")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Dr. Bambang")));
    }
}