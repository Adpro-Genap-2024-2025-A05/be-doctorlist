package id.ac.ui.cs.advprog.beprofile.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SpecialityTest {

    @Test
    void allValuesHaveDisplayNames() {
        for (Speciality s : Speciality.values()) {
            assertNotNull(s.getDisplayName(), "Display name must not be null for " + s);
            assertFalse(s.getDisplayName().trim().isEmpty(), "Display name must not be empty for " + s);
        }
    }

    @Test
    void contains_validAndInvalid() {
        Speciality first = Speciality.values()[0];
        assertTrue(Speciality.contains(first.getDisplayName()));
        assertFalse(Speciality.contains(first.getDisplayName().toLowerCase()));
        assertFalse(Speciality.contains(null));
        assertFalse(Speciality.contains("  "));
        assertFalse(Speciality.contains("Not a speciality"));
    }

    @Test
    void validatespeciality_null_throws() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> Speciality.validatespeciality(null)
        );
        assertEquals("speciality cannot be null", ex.getMessage());
    }

    @Test
    void validatespeciality_allValid_doesNotThrow() {
        for (Speciality s : Speciality.values()) {
            assertDoesNotThrow(() -> Speciality.validatespeciality(s));
        }
    }

    @Test
    void validatespeciality_valid_doesNotThrow() {
        assertDoesNotThrow(() -> Speciality.validatespeciality(Speciality.DOKTER_UMUM));
    }

    @Test
    void fromDisplayName_caseInsensitive() {
        for (Speciality s : Speciality.values()) {
            String disp = s.getDisplayName();
            assertEquals(s, Speciality.fromDisplayName(disp));
            assertEquals(s, Speciality.fromDisplayName(disp.toUpperCase()));
            assertEquals(s, Speciality.fromDisplayName(disp.toLowerCase()));
            assertThrows(IllegalArgumentException.class,
                    () -> Speciality.fromDisplayName("  " + disp + "  ")
            );
        }
    }

    @Test
    void fromDisplayName_invalid_throws() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> Speciality.fromDisplayName("Not A Real Specialty")
        );
        assertTrue(ex.getMessage().contains("No speciality found for display name"));
    }

    @Test
    void jsonValueProducesDisplayName() throws JsonProcessingException {
        ObjectMapper m = new ObjectMapper();
        for (Speciality s : Speciality.values()) {
            String json = m.writeValueAsString(s);
            assertEquals("\"" + s.getDisplayName() + "\"", json);
        }
    }

    @Test
    void jsonCreatorReadsDisplayName() throws JsonProcessingException {
        ObjectMapper m = new ObjectMapper();
        for (Speciality s : Speciality.values()) {
            String json = "\"" + s.getDisplayName() + "\"";
            Speciality parsed = m.readValue(json, Speciality.class);
            assertEquals(s, parsed);
        }
    }

}
