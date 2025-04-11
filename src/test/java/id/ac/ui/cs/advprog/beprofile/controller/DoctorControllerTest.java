package id.ac.ui.cs.advprog.beprofile.controller;

import id.ac.ui.cs.advprog.beprofile.model.Doctor;
import id.ac.ui.cs.advprog.beprofile.service.DoctorSearchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import java.util.Collections;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DoctorController.class)
public class DoctorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DoctorSearchService doctorSearchService;

    private Doctor doctor;

    @BeforeEach
    void setUp() {
        doctor = new Doctor();
        doctor.setId("doctor-123");
        doctor.setName("Dr. Bambang");
        doctor.setPracticeAddress("Jalan Bekasi Raya");
        doctor.setWorkSchedule("Mon-Fri 09:00-17:00");
        doctor.setEmail("dr.bambang@example.com");
        doctor.setPhoneNumber("081234567890");
        doctor.setRating(4.5);
    }

    @Test
    void testGetDoctorsByName() throws Exception {
        when(doctorSearchService.search("Bambang", "name"))
                .thenReturn(Collections.singletonList(doctor));

        mockMvc.perform(get("/api/doctors")
                        .param("searchType", "name")
                        .param("criteria", "Bambang")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Dr. Bambang")));

        verify(doctorSearchService, times(1)).search("Bambang", "name");
    }

    @Test
    void testGetDoctorById() throws Exception {
        when(doctorSearchService.getDoctorById("doctor-123")).thenReturn(doctor);

        mockMvc.perform(get("/api/doctors/doctor-123")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Dr. Bambang")))
                .andExpect(jsonPath("$.email", is("dr.bambang@example.com")));

        verify(doctorSearchService, times(1)).getDoctorById("doctor-123");
    }
}
