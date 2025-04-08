package id.ac.ui.cs.advprog.beprofile;

import id.ac.ui.cs.advprog.beprofile.model.Doctor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DoctorTest {

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
    void testGetId() {
        assertEquals("doctor-123", doctor.getId());
    }

    @Test
    void testGetName() {
        assertEquals("Dr. Bambang", doctor.getName());
    }

    @Test
    void testGetPracticeAddress() {
        assertEquals("Jalan Bekasi Raya", doctor.getPracticeAddress());
    }

    @Test
    void testGetWorkSchedule() {
        assertEquals("Mon-Fri 09:00-17:00", doctor.getWorkSchedule());
    }

    @Test
    void testGetEmail() {
        assertEquals("dr.bambang@example.com", doctor.getEmail());
    }

    @Test
    void testGetPhoneNumber() {
        assertEquals("081234567890", doctor.getPhoneNumber());
    }

    @Test
    void testGetRating() {
        assertEquals(4.5, doctor.getRating());
    }
}
