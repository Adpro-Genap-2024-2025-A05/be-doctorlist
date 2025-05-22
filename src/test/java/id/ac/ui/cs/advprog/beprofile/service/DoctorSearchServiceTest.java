package id.ac.ui.cs.advprog.beprofile.service;

import id.ac.ui.cs.advprog.beprofile.exception.ResourceNotFoundException;
import id.ac.ui.cs.advprog.beprofile.model.Doctor;
import id.ac.ui.cs.advprog.beprofile.repository.DoctorRepository;
import id.ac.ui.cs.advprog.beprofile.service.strategy.SearchStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class DoctorSearchServiceTest {

    @Mock
    private DoctorRepository repo;
    @Mock
    private SearchStrategy nameStrategy;

    private DoctorSearchService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new DoctorSearchService(repo, Map.of("name", nameStrategy));
    }

    @Test
    void search_delegatesToStrategy() {
        var d1 = new Doctor("doctor-1","Dr. Asep","Jalan Bekasi Raya","Mon-Fri 09:00-17:00","dr.asep@example.com","081234567891",4.2,"Cardiologists");
        var d2 = new Doctor("doctor-123","Dr. Bambang","Jalan Bekasi Raya","Mon-Fri 09:00-17:00","dr.bambang@example.com","081234567890",3.9,"Neurologists");
        when(repo.findAll()).thenReturn(List.of(d1,d2));
        when(nameStrategy.search(List.of(d1,d2), "Asep")).thenReturn(List.of(d1));

        var res = service.search("Asep", "name");

        assertThat(res).containsExactly(d1);
        verify(repo).findAll();
        verify(nameStrategy).search(any(), eq("Asep"));
    }

    @Test
    void search_unsupportedType_throws() {
        assertThatThrownBy(() -> service.search("x","foo"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("foo");
    }

    @Test
    void getDoctorById_existing_returnsDoctor() {
        var d = new Doctor("doctor-42","Dr Senku","Jalan Bekasi Raya","Mon-Fri 09:00-17:00","dr.senku@example.com","081234567892",4.7,"Radiologists");
        when(repo.findById("42")).thenReturn(Optional.of(d));

        var out = service.getDoctorById("42");
        assertThat(out).isSameAs(d);
    }

    @Test
    void getDoctorById_missing_throwsNotFound() {
        when(repo.findById("99")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getDoctorById("99"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }
}