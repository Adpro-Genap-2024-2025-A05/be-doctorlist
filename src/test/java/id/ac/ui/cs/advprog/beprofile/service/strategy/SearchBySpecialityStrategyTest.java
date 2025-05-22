package id.ac.ui.cs.advprog.beprofile.service.strategy;

import id.ac.ui.cs.advprog.beprofile.model.Doctor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SearchBySpecialityStrategyTest {
    private SearchBySpecialityStrategy strategy;
    private List<Doctor> doctors;

    @BeforeEach
    void setUp() {
        strategy = new SearchBySpecialityStrategy();
        Doctor d1 = new Doctor("1", "A", "Addr", "Mon", "a@x", "081", 4.0, "Cardiology");
        Doctor d2 = new Doctor("2", "B", "Addr", "Tue", "b@x", "081234567890", 3.5, "Neurology");
        doctors = List.of(d1, d2);
    }

    @Test
    void testEmptyCriteria() {
        assertTrue(strategy.search(doctors, "").isEmpty());
    }

    @Test
    void testMatch() {
        List<Doctor> res = strategy.search(doctors, "neuro");
        assertEquals(1, res.size());
        assertEquals("2", res.get(0).getId());
    }

    @Test
    void testNoMatch() {
        assertTrue(strategy.search(doctors, "xyz").isEmpty());
    }
}
