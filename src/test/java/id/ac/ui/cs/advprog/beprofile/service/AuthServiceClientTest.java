package id.ac.ui.cs.advprog.beprofile.service;

import id.ac.ui.cs.advprog.beprofile.dto.ApiResponseDto;
import id.ac.ui.cs.advprog.beprofile.dto.CaregiverDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class AuthServiceClientTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private AuthServiceClient client;

    private final String baseUrl = "http://auth.test";
    private final String caregiverId = UUID.randomUUID().toString();

    @BeforeEach
    void setUp() {
        org.mockito.MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(client, "authServiceBaseUrl", baseUrl);
    }

    @Test
    void getAllCaregivers_successfulResponse_returnsList() {
        CaregiverDto dto = CaregiverDto.builder().id(caregiverId).build();
        ApiResponseDto<List<CaregiverDto>> api = ApiResponseDto.success(200, "ok", List.of(dto));
        ResponseEntity<ApiResponseDto<List<CaregiverDto>>> resp = ResponseEntity.ok(api);

        when(restTemplate.exchange(
                eq(baseUrl + "/data"),
                eq(HttpMethod.GET),
                isNull(),
                any(ParameterizedTypeReference.class)
        )).thenReturn(resp);

        List<CaregiverDto> result = client.getAllCaregivers();
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(dto, result.get(0));
    }

    @Test
    void getAllCaregivers_nullBody_returnsEmptyList() {
        ResponseEntity<ApiResponseDto<List<CaregiverDto>>> resp = ResponseEntity.ok(null);
        when(restTemplate.exchange(anyString(), any(), any(), any(ParameterizedTypeReference.class)))
                .thenReturn(resp);

        List<CaregiverDto> result = client.getAllCaregivers();
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getAllCaregivers_nullData_returnsEmptyList() {
        ApiResponseDto<List<CaregiverDto>> api = ApiResponseDto.success(200, "ok", null);
        ResponseEntity<ApiResponseDto<List<CaregiverDto>>> resp = ResponseEntity.ok(api);
        when(restTemplate.exchange(anyString(), any(), any(), any(ParameterizedTypeReference.class)))
                .thenReturn(resp);

        List<CaregiverDto> result = client.getAllCaregivers();
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getAllCaregivers_exception_throwsRuntimeException() {
        when(restTemplate.exchange(anyString(), any(), any(), any(ParameterizedTypeReference.class)))
                .thenThrow(new RuntimeException("fail"));

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> client.getAllCaregivers()
        );
        assertTrue(ex.getMessage().contains("Failed to fetch caregivers from auth service"));
    }

    @Test
    void searchCaregivers_withParams_successfulResponse_returnsList() {
        CaregiverDto dto = CaregiverDto.builder().id(caregiverId).build();
        ApiResponseDto<List<CaregiverDto>> api = ApiResponseDto.success(200, "ok", List.of(dto));
        ResponseEntity<ApiResponseDto<List<CaregiverDto>>> resp = ResponseEntity.ok(api);

        when(restTemplate.exchange(
                startsWith(baseUrl + "/data/search"),
                eq(HttpMethod.GET),
                isNull(),
                any(ParameterizedTypeReference.class)
        )).thenReturn(resp);

        List<CaregiverDto> result = client.searchCaregivers("Alice", "Spec");
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(dto, result.get(0));
    }

    @Test
    void searchCaregivers_emptyParams_successfulResponse_returnsList() {
        CaregiverDto dto = CaregiverDto.builder().id(caregiverId).build();
        ApiResponseDto<List<CaregiverDto>> api = ApiResponseDto.success(200, "ok", List.of(dto));
        ResponseEntity<ApiResponseDto<List<CaregiverDto>>> resp = ResponseEntity.ok(api);

        when(restTemplate.exchange(
                eq(baseUrl + "/data/search"),
                eq(HttpMethod.GET),
                isNull(),
                any(ParameterizedTypeReference.class)
        )).thenReturn(resp);

        List<CaregiverDto> result = client.searchCaregivers(null, null);
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void searchCaregivers_nullData_returnsEmptyList() {
        ApiResponseDto<List<CaregiverDto>> api = ApiResponseDto.success(200, "ok", null);
        ResponseEntity<ApiResponseDto<List<CaregiverDto>>> resp = ResponseEntity.ok(api);
        when(restTemplate.exchange(anyString(), any(), any(), any(ParameterizedTypeReference.class)))
                .thenReturn(resp);

        List<CaregiverDto> result = client.searchCaregivers("Name", "Spec");
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void searchCaregivers_exception_throwsRuntimeException() {
        when(restTemplate.exchange(anyString(), any(), any(), any(ParameterizedTypeReference.class)))
                .thenThrow(new RuntimeException("fail"));

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> client.searchCaregivers("Name", "Spec")
        );
        assertTrue(ex.getMessage().contains("Failed to search caregivers from auth service"));
    }

    @Test
    void getCaregiverById_successfulResponse_returnsDto() {
        CaregiverDto dto = CaregiverDto.builder().id(caregiverId).build();
        ApiResponseDto<CaregiverDto> api = ApiResponseDto.success(200, "ok", dto);
        ResponseEntity<ApiResponseDto<CaregiverDto>> resp = ResponseEntity.ok(api);

        when(restTemplate.exchange(
                eq(baseUrl + "/data/" + caregiverId),
                eq(HttpMethod.GET),
                isNull(),
                any(ParameterizedTypeReference.class)
        )).thenReturn(resp);

        CaregiverDto result = client.getCaregiverById(caregiverId);
        assertNotNull(result);
        assertEquals(dto, result);
    }

    @Test
    void getCaregiverById_dataNull_throwsRuntimeException() {
        ApiResponseDto<CaregiverDto> api = ApiResponseDto.success(200, "ok", null);
        ResponseEntity<ApiResponseDto<CaregiverDto>> resp = ResponseEntity.ok(api);
        when(restTemplate.exchange(anyString(), any(), any(), any(ParameterizedTypeReference.class)))
                .thenReturn(resp);

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> client.getCaregiverById(caregiverId)
        );
        assertTrue(ex.getMessage().contains("Failed to fetch caregiver by id from auth service"));
    }

    @Test
    void getCaregiverById_exception_throwsRuntimeException() {
        when(restTemplate.exchange(anyString(), any(), any(), any(ParameterizedTypeReference.class)))
                .thenThrow(new RuntimeException("fail"));

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> client.getCaregiverById(caregiverId)
        );
        assertTrue(ex.getMessage().contains("Failed to fetch caregiver by id from auth service"));
    }
}