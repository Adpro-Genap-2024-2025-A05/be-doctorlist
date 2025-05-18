package id.ac.ui.cs.advprog.beprofile.repository;

import id.ac.ui.cs.advprog.beprofile.model.Doctor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DoctorRepositoryTest {

    @Autowired
    private DoctorRepository repo;

    @BeforeEach
    void setUp() {
        repo.deleteAll();
        var d = new Doctor();
        d.setId("doctor-123");
        d.setName("Dr. Bambang");
        d.setPracticeAddress("Test Address");
        d.setWorkSchedule("Mon-Fri 09:00-17:00");
        d.setEmail("dr.bambang@example.com");
        d.setPhoneNumber("081234567890");
        d.setRating(5.0);
        d.setSpeciality("Neurosurgeon");
        repo.save(d);
    }

    @Test
    void findAll_shouldReturnOne() {
        var all = repo.findAll();
        assertThat(all).hasSize(1)
                .first()
                .extracting(Doctor::getId, Doctor::getName)
                .containsExactly("doctor-123","Dr. Bambang");
    }

    @Test
    void findById_existing_shouldReturn() {
        Optional<Doctor> found = repo.findById("doctor-123");
        assertThat(found).isPresent()
                .get()
                .extracting(Doctor::getEmail, Doctor::getSpeciality)
                .containsExactly("dr.bambang@example.com","Neurosurgeon");
    }

    @Test
    void findById_missing_shouldBeEmpty() {
        assertThat(repo.findById("nope")).isEmpty();
    }
}
