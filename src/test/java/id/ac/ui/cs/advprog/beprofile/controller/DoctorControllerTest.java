package id.ac.ui.cs.advprog.beprofile.controller;

import id.ac.ui.cs.advprog.beprofile.model.Doctor;
import id.ac.ui.cs.advprog.beprofile.service.DoctorSearchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DoctorController.class)
public class DoctorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private DoctorSearchService doctorSearchService;

    private List<Doctor> doctors;

    @BeforeEach
    void setUp() {
        // Inisialisasi DoctorSearchService
        doctorSearchService = new DoctorSearchService();

        // Tambahkan data dokter ke dalam layanan
        doctors = new ArrayList<>();
        Doctor doctor = new Doctor();
        doctor.setId("doctor-123");
        doctor.setName("Dr. Bambang");
        doctor.setPracticeAddress("Jalan Bekasi Raya");
        doctor.setWorkSchedule("Mon-Fri 09:00-17:00");
        doctor.setEmail("dr.bambang@example.com");
        doctor.setPhoneNumber("081234567890");
        doctor.setRating(4.5);
        doctors.add(doctor);

        doctorSearchService.addDoctor(doctor);
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