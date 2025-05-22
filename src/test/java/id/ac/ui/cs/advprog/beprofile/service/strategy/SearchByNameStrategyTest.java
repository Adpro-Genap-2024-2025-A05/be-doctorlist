package id.ac.ui.cs.advprog.beprofile.service.strategy;

import id.ac.ui.cs.advprog.beprofile.model.Doctor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SearchByNameStrategyTest {
    private SearchByNameStrategy strategy;
    private List<Doctor> doctors;

    @BeforeEach
    void setUp() {
        strategy = new SearchByNameStrategy();
        Doctor d1 = new Doctor();
        d1.setId("1");
        d1.setName("Alice Smith");
        d1.setPracticeAddress("Addr1");
        d1.setSpeciality("Cardiology");
        d1.setWorkSchedule("Mon-Fri 09:00-17:00");
        d1.setEmail("alice@example.com");
        d1.setPhoneNumber("081234");
        d1.setRating(4.2);

        Doctor d2 = new Doctor();
        d2.setId("2");
        d2.setName("Bob Jones");
        d2.setPracticeAddress("Addr2");
        d2.setSpeciality("Neurology");
        d2.setWorkSchedule("Sat-Sun 10:00-14:00");
        d2.setEmail("bob@example.com");
        d2.setPhoneNumber("082345");
        d2.setRating(3.8);

        doctors = List.of(d1, d2);
    }

    @Test
    void testEmptyCriteriaReturnsEmptyList() {
        List<Doctor> result = strategy.search(doctors, "");
        assertTrue(result.isEmpty(), "Empty criteria should yield no results");
    }

    @Test
    void testNullCriteriaReturnsEmptyList() {
        List<Doctor> result = strategy.search(doctors, null);
        assertTrue(result.isEmpty(), "Null criteria should yield no results");
    }

    @Test
    void testMatchSingleDoctor() {
        List<Doctor> result = strategy.search(doctors, "alice");
        assertEquals(1, result.size(), "Should find one match for 'alice'");
        assertEquals("1", result.get(0).getId());
    }

    @Test
    void testCaseInsensitiveMatch() {
        List<Doctor> result = strategy.search(doctors, "BOB");
        assertEquals(1, result.size(), "Should match regardless of case");
        assertEquals("2", result.get(0).getId());
    }

    @Test
    void testNoMatchReturnsEmptyList() {
        List<Doctor> result = strategy.search(doctors, "charlie");
        assertTrue(result.isEmpty(), "No doctor named 'charlie' should return empty list");
    }
}
