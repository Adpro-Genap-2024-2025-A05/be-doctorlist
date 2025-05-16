package id.ac.ui.cs.advprog.beprofile.service;

import id.ac.ui.cs.advprog.beprofile.model.Doctor;
import id.ac.ui.cs.advprog.beprofile.repository.InMemoryDoctorRepository;
import id.ac.ui.cs.advprog.beprofile.repository.DoctorRepository;
import id.ac.ui.cs.advprog.beprofile.service.strategy.SearchByNameStrategy;
import id.ac.ui.cs.advprog.beprofile.service.strategy.SearchStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DoctorSearchServiceTest {

    private DoctorSearchService service;

    @BeforeEach
    void setup() {
        DoctorRepository repo = new InMemoryDoctorRepository();
        Map<String, SearchStrategy> strategies = new HashMap<>();
        strategies.put("name", new SearchByNameStrategy());

        service = new DoctorSearchService(repo, strategies);

        Doctor doctor = new Doctor();
        doctor.setId("doctor-123");
        doctor.setName("Dr. Bambang");
        doctor.setPracticeAddress("Jalan Bekasi Raya");
        doctor.setWorkSchedule("Mon-Fri 09:00-17:00");
        doctor.setEmail("dr.bambang@example.com");
        doctor.setPhoneNumber("081234567890");
        doctor.setRating(4.5);
        service.addDoctor(doctor);
    }

    @Test
    void testSearchByName() {
        List<Doctor> result = service.search("Bambang", "name");
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Dr. Bambang", result.get(0).getName());
    }

    @Test
    void testSearchWithUnsupportedType() {
        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                service.search("Bambang", "unsupported"));
        assertTrue(ex.getMessage().contains("unsupported"));
    }

    @Test
    void testGetDoctorByIdFound() {
        Doctor d = service.getDoctorById("doctor-123");
        assertNotNull(d);
        assertEquals("Dr. Bambang", d.getName());
    }

    @Test
    void testGetDoctorByIdNotFound() {
        assertNull(service.getDoctorById("no-such-id"));
    }
}
