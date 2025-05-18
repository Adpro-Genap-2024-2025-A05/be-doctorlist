package id.ac.ui.cs.advprog.beprofile.controller;

import id.ac.ui.cs.advprog.beprofile.exception.ResourceNotFoundException;
import id.ac.ui.cs.advprog.beprofile.model.Doctor;
import id.ac.ui.cs.advprog.beprofile.service.DoctorSearchService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DoctorController.class)
class DoctorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // yes, @MockBean is deprecated in 3.4.0+ but it still works and
    // is by far the easiest way to wire in your service here.
    @MockBean
    private DoctorSearchService service;

    @Test
    void testGetDoctorsByName() throws Exception {
        var d = new Doctor("doctor-123",
                "Dr. Bambang",
                "Jalan Bekasi Raya",
                "Mon-Fri 09:00-17:00",
                "dr.bambang@example.com",
                "081234567890",
                4.5,
                "Cardiology");
        given(service.search("Bambang","name")).willReturn(List.of(d));

        mockMvc.perform(get("/api/doctors")
                        .param("searchType","name")
                        .param("criteria","Bambang")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("doctor-123"))
                .andExpect(jsonPath("$[0].name").value("Dr. Bambang"))
                .andExpect(jsonPath("$[0].rating").value(4.5));
    }

    @Test
    void testGetDoctorById_ok() throws Exception {
        var d = new Doctor("doctor-123",
                "Dr. Bambang",
                "Jalan Bekasi Raya",
                "Mon-Fri 09:00-17:00",
                "dr.bambang@example.com",
                "081234567890",
                4.5,
                "Cardiology");
        given(service.getDoctorById("doctor-123")).willReturn(d);

        mockMvc.perform(get("/api/doctors/doctor-123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("dr.bambang@example.com"))
                .andExpect(jsonPath("$.speciality").value("Cardiology"));
    }

    @Test
    void testGetDoctorById_notFound() throws Exception {
        willThrow(new ResourceNotFoundException("not found"))
                .given(service).getDoctorById("nope");

        mockMvc.perform(get("/api/doctors/nope"))
                .andExpect(status().isNotFound());
    }
}
