package id.ac.ui.cs.advprog.beprofile.service;

import id.ac.ui.cs.advprog.beprofile.dto.ApiResponseDto;
import id.ac.ui.cs.advprog.beprofile.dto.ScheduleDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class KonsultasiServiceClientTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private KonsultasiServiceClient client;

    private final String baseUrl = "http://konsultasi.test";
    private final String caregiverId = UUID.randomUUID().toString();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(client, "konsultasiServiceBaseUrl", baseUrl);
    }

    @Test
    void getCaregiverSchedules_successfulResponse_returnsData() {
        ScheduleDto dto = ScheduleDto.builder()
                .id(UUID.fromString(caregiverId))
                .caregiverId(UUID.fromString(caregiverId))
                .day(DayOfWeek.MONDAY)
                .startTime(LocalTime.of(9, 0))
                .endTime(LocalTime.of(17, 0))
                .specificDate(LocalDate.now())
                .oneTime(false)
                .build();
        ApiResponseDto<List<ScheduleDto>> api = ApiResponseDto.success(200, "ok", List.of(dto));
        ResponseEntity<ApiResponseDto<List<ScheduleDto>>> resp = ResponseEntity.ok(api);

        when(restTemplate.exchange(
                eq(baseUrl + "/data/caregiver/" + caregiverId),
                eq(HttpMethod.GET),
                isNull(),
                any(ParameterizedTypeReference.class)
        )).thenReturn(resp);

        List<ScheduleDto> result = client.getCaregiverSchedules(caregiverId);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(dto, result.get(0));
    }

    @Test
    void getCaregiverSchedules_nullBody_returnsEmptyList() {
        ResponseEntity<ApiResponseDto<List<ScheduleDto>>> resp = ResponseEntity.ok(null);
        when(restTemplate.exchange(anyString(), any(), any(), any(ParameterizedTypeReference.class)))
                .thenReturn(resp);

        List<ScheduleDto> result = client.getCaregiverSchedules(caregiverId);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getCaregiverSchedules_exception_returnsEmptyList() {
        when(restTemplate.exchange(anyString(), any(), any(), any(ParameterizedTypeReference.class)))
                .thenThrow(new RuntimeException("fail"));

        List<ScheduleDto> result = client.getCaregiverSchedules(caregiverId);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getSchedulesForCaregivers_withIds_successfulResponse_returnsData() {
        List<String> ids = List.of(caregiverId, "other");
        ScheduleDto dto = ScheduleDto.builder()
                .id(UUID.fromString(caregiverId))
                .caregiverId(UUID.fromString(caregiverId))
                .day(DayOfWeek.MONDAY)
                .startTime(LocalTime.of(9, 0))
                .endTime(LocalTime.of(17, 0))
                .specificDate(LocalDate.now())
                .oneTime(false)
                .build();
        ApiResponseDto<List<ScheduleDto>> api = ApiResponseDto.success(200, "ok", List.of(dto));
        ResponseEntity<ApiResponseDto<List<ScheduleDto>>> resp = ResponseEntity.ok(api);

        when(restTemplate.exchange(
                startsWith(baseUrl + "/data/schedules"),
                eq(HttpMethod.GET),
                isNull(),
                any(ParameterizedTypeReference.class)
        )).thenReturn(resp);

        List<ScheduleDto> result = client.getSchedulesForCaregivers(ids);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(dto, result.get(0));
    }

    @Test
    void getSchedulesForCaregivers_nullData_returnsEmptyList() {
        ResponseEntity<ApiResponseDto<List<ScheduleDto>>> resp = ResponseEntity.ok(ApiResponseDto.success(200, "ok", null));
        when(restTemplate.exchange(anyString(), any(), any(), any(ParameterizedTypeReference.class)))
                .thenReturn(resp);

        List<ScheduleDto> result = client.getSchedulesForCaregivers(List.of(caregiverId));
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getSchedulesForCaregivers_exception_fallsBackToCaregiverSchedules() {
        List<String> ids = List.of(caregiverId);
        when(restTemplate.exchange(anyString(), any(), any(), any(ParameterizedTypeReference.class)))
                .thenThrow(new RuntimeException("error"));
        // spy on new method
        KonsultasiServiceClient spy = Mockito.spy(client);
        ScheduleDto dto = ScheduleDto.builder()
                .id(UUID.fromString(caregiverId))
                .caregiverId(UUID.fromString(caregiverId))
                .day(DayOfWeek.MONDAY)
                .startTime(LocalTime.of(9, 0))
                .endTime(LocalTime.of(17, 0))
                .specificDate(LocalDate.now())
                .oneTime(false)
                .build();
        when(spy.getCaregiverSchedules(caregiverId)).thenReturn(List.of(dto));

        List<ScheduleDto> result = spy.getSchedulesForCaregivers(ids);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(dto, result.get(0));
    }

    @Test
    void getAvailableSchedules_delegatesToNewMethod() {
        List<String> ids = List.of(caregiverId);
        KonsultasiServiceClient spy = Mockito.spy(client);


        Mockito.doReturn(List.of()).when(spy).getSchedulesForCaregivers(ids);

        List<ScheduleDto> result = spy.getAvailableSchedules(ids);

        assertNotNull(result);
        Mockito.verify(spy).getSchedulesForCaregivers(ids);
    }
}
