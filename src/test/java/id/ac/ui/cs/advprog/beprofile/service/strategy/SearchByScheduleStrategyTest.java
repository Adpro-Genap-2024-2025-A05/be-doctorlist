package id.ac.ui.cs.advprog.beprofile.service.strategy;

import id.ac.ui.cs.advprog.beprofile.model.Doctor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SearchByScheduleStrategyTest {
    private SearchByScheduleStrategy strategy;
    private List<Doctor> doctors;

    @BeforeEach
    void setUp() {
        strategy = new SearchByScheduleStrategy();
        Doctor d1 = new Doctor("1", "Asep", "Addr", "Mon-Fri 09:00-17:00", "a@x", "081234567891", 4.0, "Cardiology");
        Doctor d2 = new Doctor("2", "Bambang", "Addr", "Sat-Sun 10:00-14:00", "b@x", "081234567890", 3.5, "Neurology");
        doctors = List.of(d1, d2);
    }

    @Test
    void testEmptyCriteria() {
        assertTrue(strategy.search(doctors, null).isEmpty());
    }

    @Test
    void testMatch() {
        List<Doctor> res = strategy.search(doctors, "fri");
        assertEquals(1, res.size());
        assertEquals("1", res.get(0).getId());
    }

    @Test
    void testNoMatch() {
        assertTrue(strategy.search(doctors, "xyz").isEmpty());
    }
}