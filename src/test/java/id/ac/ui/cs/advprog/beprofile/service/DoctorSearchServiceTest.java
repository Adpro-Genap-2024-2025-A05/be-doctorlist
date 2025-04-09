package id.ac.ui.cs.advprog.beprofile.service;

import com.example.doctorprofile.model.Doctor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class DoctorSearchServiceTest {

    private DoctorSearchService service;

    @BeforeEach
    void setup() {
        service = new DoctorSearchService();
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
        assertNotNull(result, "The search result should not be null");
        assertEquals(1, result.size(), "Search by name should return one doctor");
        assertEquals("Dr. Bambang", result.get(0).getName(), "Doctor's name should be 'Dr. Bambang'");
    }

    @Test
    void testSearchWithUnsupportedType() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                service.search("Bambang", "unsupported-type")
        );
        String expectedMessage = "Search type unsupported-type is not supported.";
        assertTrue(exception.getMessage().contains(expectedMessage));
    }
}
