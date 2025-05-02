package id.ac.ui.cs.advprog.beprofile.repository;

import id.ac.ui.cs.advprog.beprofile.model.Doctor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Iterator;
import static org.junit.jupiter.api.Assertions.*;

public class InMemoryDoctorRepositoryTest {
    private DoctorRepository doctorRepository;
    private Doctor doctor;

    @BeforeEach
    void setup() {
        doctorRepository = new InMemoryDoctorRepository();
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
    void testCreateAndFindById() {
        doctorRepository.create(doctor);
        Doctor found = doctorRepository.findById("doctor-123");
        assertNotNull(found);
        assertEquals("Dr. Bambang", found.getName());
    }

    @Test
    void testFindAllIfEmpty() {
        Iterator<Doctor> iterator = doctorRepository.findAll();
        assertFalse(iterator.hasNext());
    }

    @Test
    void testFindAllIfMoreThanOneDoctor() {
        doctorRepository.create(doctor);
        Doctor doctor2 = new Doctor();
        doctor2.setId("doctor-456");
        doctor2.setName("Dr. Siti");
        doctorRepository.create(doctor2);
        Iterator<Doctor> iterator = doctorRepository.findAll();

        assertTrue(iterator.hasNext());
        Doctor savedDoctor = iterator.next();
        assertNotNull(savedDoctor.getId());
        if (savedDoctor.getId().equals("doctor-123")) {
            assertEquals("Dr. Bambang", savedDoctor.getName());
        }
        if (iterator.hasNext()) {
            Doctor savedDoctor2 = iterator.next();
            if (savedDoctor2.getId().equals("doctor-456")) {
                assertEquals("Dr. Siti", savedDoctor2.getName());
            }
        }
    }

    @Test
    void testUpdateDoctor() {
        doctorRepository.create(doctor);

        Doctor updatedDoctor = new Doctor();
        updatedDoctor.setId("doctor-123");
        updatedDoctor.setName("Dr. Bambang Baru");
        updatedDoctor.setPracticeAddress("Jalan Bekasi Raya");
        updatedDoctor.setWorkSchedule("Mon-Fri 09:00-17:00");
        updatedDoctor.setEmail("dr.bambangbaru@example.com");
        updatedDoctor.setPhoneNumber("081234567890");
        updatedDoctor.setRating(4.7);

        Doctor result = doctorRepository.update("doctor-123", updatedDoctor);
        assertNotNull(result);
        assertEquals("Dr. Bambang Baru", result.getName());
        assertEquals(4.7, result.getRating());
    }

    @Test
    void testDeleteDoctor() {
        doctorRepository.create(doctor);
        Doctor deleted = doctorRepository.delete("doctor-123");
        assertNotNull(deleted);
        assertEquals("doctor-123", deleted.getId());
        assertNull(doctorRepository.findById("doctor-123"));
    }

    @Test
    void testDeleteNonExistentDoctor() {
        Doctor deleted = doctorRepository.delete("non-existent-id");
        assertNull(deleted);
    }

    @Test
    void testClearWipesOutAllDoctors() {
        doctorRepository.create(doctor);
        Doctor doctor2 = new Doctor();
        doctor2.setId("doctor-456");
        doctor2.setName("Dr. Siti");
        doctorRepository.create(doctor2);

        assertTrue(doctorRepository.findAll().hasNext());

        ((InMemoryDoctorRepository)doctorRepository).clear();

        Iterator<Doctor> afterClear = doctorRepository.findAll();
        assertFalse(afterClear.hasNext(), "clear() should remove all entries");
        assertNull(doctorRepository.findById("doctor-123"));
        assertNull(doctorRepository.findById("doctor-456"));
    }
}
