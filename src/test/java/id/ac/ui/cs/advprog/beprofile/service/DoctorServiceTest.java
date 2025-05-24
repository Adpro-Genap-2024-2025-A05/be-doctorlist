package id.ac.ui.cs.advprog.beprofile.service;

import id.ac.ui.cs.advprog.beprofile.dto.*;
import id.ac.ui.cs.advprog.beprofile.enums.Speciality;
import id.ac.ui.cs.advprog.beprofile.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class DoctorServiceTest {

    @Mock
    private AuthServiceClient authServiceClient;

    @Mock
    private KonsultasiServiceClient konsultasiServiceClient;

    @Mock
    private RatingServiceClient ratingServiceClient;

    @InjectMocks
    private DoctorService doctorService;

    private CaregiverDto sampleCaregiver;
    private String caregiverId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(ratingServiceClient.getCaregiverRatingStats(anyString()))
                .thenReturn(CaregiverRatingStatsDto.builder()
                        .averageRating(0.0)
                        .totalRatings(0L)
                        .build());

        caregiverId = UUID.randomUUID().toString();
        sampleCaregiver = CaregiverDto.builder()
                .id(caregiverId)
                .name("Alice")
                .email("alice@example.com")
                .speciality(Speciality.SPESIALIS_JANTUNG)
                .workAddress("Hospital A")
                .phoneNumber("123456789")
                .build();
    }

    @Test
    void searchDoctors_noFilters_returnsAllPaged() {
        when(authServiceClient.getAllCaregivers()).thenReturn(List.of(sampleCaregiver));
        when(konsultasiServiceClient.getCaregiverSchedules(caregiverId)).thenReturn(List.of());

        Page<DoctorResponseDto> page = doctorService.searchDoctors(
                DoctorSearchRequestDto.builder().page(0).size(10).build());

        assertEquals(1, page.getTotalElements());
    }

    @Test
    void searchDoctors_nameFilter_usesSearchCaregivers() {
        when(authServiceClient.searchCaregivers("Bob", null)).thenReturn(List.of(sampleCaregiver));
        when(konsultasiServiceClient.getCaregiverSchedules(caregiverId)).thenReturn(List.of());

        Page<DoctorResponseDto> page = doctorService.searchDoctors(
                DoctorSearchRequestDto.builder()
                        .name("Bob")
                        .page(0)
                        .size(5)
                        .build());

        assertEquals(1, page.getTotalElements());
    }

    @Test
    void searchDoctors_specialityFilter_usesSearchCaregivers() {
        when(authServiceClient.searchCaregivers(null, Speciality.SPESIALIS_JANTUNG)).thenReturn(List.of(sampleCaregiver));
        when(konsultasiServiceClient.getCaregiverSchedules(caregiverId)).thenReturn(List.of());

        Page<DoctorResponseDto> page = doctorService.searchDoctors(
                DoctorSearchRequestDto.builder()
                        .speciality(Speciality.SPESIALIS_JANTUNG)
                        .page(0)
                        .size(5)
                        .build());

        assertEquals(1, page.getTotalElements());
    }

    @Test
    void searchDoctors_withScheduleFilter_filtersByDay() {
        when(authServiceClient.getAllCaregivers()).thenReturn(List.of(sampleCaregiver));
        ScheduleDto sched = ScheduleDto.builder()
                .id(UUID.randomUUID())
                .caregiverId(UUID.fromString(caregiverId))
                .day(DayOfWeek.MONDAY)
                .startTime(LocalTime.of(8,0))
                .endTime(LocalTime.of(12,0))
                .specificDate(LocalDate.now())
                .oneTime(false)
                .build();

        when(konsultasiServiceClient.getSchedulesForCaregivers(List.of(caregiverId)))
                .thenReturn(List.of(sched));
        when(konsultasiServiceClient.getCaregiverSchedules(caregiverId)).thenReturn(List.of(sched));

        Page<DoctorResponseDto> page = doctorService.searchDoctors(
                DoctorSearchRequestDto.builder()
                        .workingSchedule("MONDAY")
                        .page(0)
                        .size(10)
                        .build());

        assertEquals(1, page.getTotalElements());
    }

    @Test
    void searchDoctors_scheduleServiceThrows_returnsOriginalList() {
        when(authServiceClient.getAllCaregivers()).thenReturn(List.of(sampleCaregiver));
        when(konsultasiServiceClient.getSchedulesForCaregivers(any()))
                .thenThrow(new RuntimeException("err"));
        when(konsultasiServiceClient.getCaregiverSchedules(caregiverId)).thenReturn(List.of());

        Page<DoctorResponseDto> page = doctorService.searchDoctors(
                DoctorSearchRequestDto.builder()
                        .workingSchedule("MONDAY")
                        .page(0)
                        .size(10)
                        .build());

        assertEquals(1, page.getTotalElements());
    }

    @Test
    void searchDoctors_invalidSchedule_returnsUnfiltered() {
        when(authServiceClient.getAllCaregivers()).thenReturn(List.of(sampleCaregiver));

        ScheduleDto sched = ScheduleDto.builder()
                .id(UUID.randomUUID())
                .caregiverId(UUID.fromString(caregiverId))
                .day(DayOfWeek.MONDAY)
                .startTime(LocalTime.of(8, 0))
                .endTime(LocalTime.of(12, 0))
                .specificDate(LocalDate.now())
                .oneTime(false)
                .build();
        when(konsultasiServiceClient.getSchedulesForCaregivers(List.of(caregiverId)))
                .thenReturn(List.of(sched));

        when(konsultasiServiceClient.getCaregiverSchedules(caregiverId))
                .thenReturn(List.of(sched));

        Page<DoctorResponseDto> page = doctorService.searchDoctors(
                DoctorSearchRequestDto.builder()
                        .workingSchedule("NOT_A_DAY")
                        .page(0)
                        .size(10)
                        .build());

        assertEquals(1, page.getTotalElements());
    }

    @Test
    void searchDoctors_pagingDefaults_whenNullOrInvalid() {
        when(authServiceClient.getAllCaregivers()).thenReturn(List.of(sampleCaregiver));
        when(konsultasiServiceClient.getCaregiverSchedules(caregiverId)).thenReturn(List.of());

        Page<DoctorResponseDto> page1 = doctorService.searchDoctors(
                DoctorSearchRequestDto.builder().build());
        assertEquals(1, page1.getTotalElements());

        Page<DoctorResponseDto> page2 = doctorService.searchDoctors(
                DoctorSearchRequestDto.builder()
                        .page(-5)
                        .size(500)
                        .build());
        assertEquals(1, page2.getTotalElements());
    }

    @Test
    void searchDoctors_exception_throwsRuntimeException() {
        when(authServiceClient.getAllCaregivers()).thenThrow(new RuntimeException("fail"));
        assertThrows(RuntimeException.class, () ->
                doctorService.searchDoctors(DoctorSearchRequestDto.builder().page(0).size(1).build())
        );
    }

    @Test
    void getDoctorById_success_returnsDto() {
        when(authServiceClient.getCaregiverById(caregiverId)).thenReturn(sampleCaregiver);
        when(konsultasiServiceClient.getCaregiverSchedules(caregiverId)).thenReturn(List.of());

        DoctorResponseDto dto = doctorService.getDoctorById(caregiverId);
        assertEquals(caregiverId, dto.getId());
    }

    @Test
    void getDoctorById_notFound_throwsResourceNotFoundException() {
        when(authServiceClient.getCaregiverById(caregiverId)).thenThrow(new RuntimeException("nope"));
        assertThrows(ResourceNotFoundException.class, () ->
                doctorService.getDoctorById(caregiverId)
        );
    }

    @Test
    void searchDoctors_withWorkingDayFilter_filtersByWorkingDay() {
        when(authServiceClient.getAllCaregivers()).thenReturn(List.of(sampleCaregiver));
        ScheduleDto sched = ScheduleDto.builder()
                .id(UUID.randomUUID())
                .caregiverId(UUID.fromString(caregiverId))
                .day(DayOfWeek.TUESDAY)
                .startTime(LocalTime.of(9, 0))
                .endTime(LocalTime.of(17, 0))
                .specificDate(LocalDate.now())
                .oneTime(false)
                .build();
        when(konsultasiServiceClient.getSchedulesForCaregivers(List.of(caregiverId)))
                .thenReturn(List.of(sched));
        when(konsultasiServiceClient.getCaregiverSchedules(caregiverId)).thenReturn(List.of(sched));

        Page<DoctorResponseDto> page = doctorService.searchDoctors(
                DoctorSearchRequestDto.builder()
                        .workingDay(DayOfWeek.TUESDAY)
                        .page(0)
                        .size(10)
                        .build());

        assertEquals(1, page.getTotalElements());

        page = doctorService.searchDoctors(
                DoctorSearchRequestDto.builder()
                        .workingDay(DayOfWeek.MONDAY)
                        .page(0)
                        .size(10)
                        .build());
        assertEquals(0, page.getTotalElements());
    }

    @Test
    void searchDoctors_withStartTimeOnly_filtersOutSchedulesBeforeOrAfter() {
        when(authServiceClient.getAllCaregivers()).thenReturn(List.of(sampleCaregiver));
        ScheduleDto sched = ScheduleDto.builder()
                .id(UUID.randomUUID())
                .caregiverId(UUID.fromString(caregiverId))
                .day(DayOfWeek.WEDNESDAY)
                .startTime(LocalTime.of(10, 0))
                .endTime(LocalTime.of(12, 0))
                .specificDate(LocalDate.now())
                .oneTime(false)
                .build();
        when(konsultasiServiceClient.getSchedulesForCaregivers(List.of(caregiverId)))
                .thenReturn(List.of(sched));
        when(konsultasiServiceClient.getCaregiverSchedules(caregiverId)).thenReturn(List.of(sched));

        Page<DoctorResponseDto> page = doctorService.searchDoctors(
                DoctorSearchRequestDto.builder()
                        .startTime(LocalTime.of(11, 0))
                        .page(0)
                        .size(10)
                        .build());
        assertEquals(1, page.getTotalElements());

        page = doctorService.searchDoctors(
                DoctorSearchRequestDto.builder()
                        .startTime(LocalTime.of(9, 0))
                        .page(0)
                        .size(10)
                        .build());
        assertEquals(0, page.getTotalElements());
        page = doctorService.searchDoctors(
                DoctorSearchRequestDto.builder()
                        .startTime(LocalTime.of(12, 0))
                        .page(0)
                        .size(10)
                        .build());
        assertEquals(0, page.getTotalElements());
    }

    @Test
    void searchDoctors_workingScheduleMismatch_filtersOut() {
        when(authServiceClient.getAllCaregivers()).thenReturn(List.of(sampleCaregiver));
        ScheduleDto sched = ScheduleDto.builder()
                .id(UUID.randomUUID())
                .caregiverId(UUID.fromString(caregiverId))
                .day(DayOfWeek.MONDAY)
                .startTime(LocalTime.of(8, 0))
                .endTime(LocalTime.of(12, 0))
                .specificDate(LocalDate.now())
                .oneTime(false)
                .build();
        when(konsultasiServiceClient.getSchedulesForCaregivers(List.of(caregiverId)))
                .thenReturn(List.of(sched));
        when(konsultasiServiceClient.getCaregiverSchedules(caregiverId))
                .thenReturn(List.of(sched));

        Page<DoctorResponseDto> page = doctorService.searchDoctors(
                DoctorSearchRequestDto.builder()
                        .workingSchedule("TUESDAY")
                        .page(0)
                        .size(10)
                        .build());

        assertEquals(0, page.getTotalElements());
    }

    @Test
    void searchDoctors_withEndTimeOnly_filtersOutSchedulesBeforeOrAfter() {
        when(authServiceClient.getAllCaregivers()).thenReturn(List.of(sampleCaregiver));
        ScheduleDto sched = ScheduleDto.builder()
                .id(UUID.randomUUID())
                .caregiverId(UUID.fromString(caregiverId))
                .day(DayOfWeek.THURSDAY)
                .startTime(LocalTime.of(14, 0))
                .endTime(LocalTime.of(16, 0))
                .specificDate(LocalDate.now())
                .oneTime(false)
                .build();
        when(konsultasiServiceClient.getSchedulesForCaregivers(List.of(caregiverId)))
                .thenReturn(List.of(sched));
        when(konsultasiServiceClient.getCaregiverSchedules(caregiverId))
                .thenReturn(List.of(sched));

        Page<DoctorResponseDto> pageMatch = doctorService.searchDoctors(
                DoctorSearchRequestDto.builder()
                        .endTime(LocalTime.of(15, 0))
                        .page(0)
                        .size(10)
                        .build());
        assertEquals(1, pageMatch.getTotalElements());

        Page<DoctorResponseDto> pageLow = doctorService.searchDoctors(
                DoctorSearchRequestDto.builder()
                        .endTime(LocalTime.of(14, 0))
                        .page(0)
                        .size(10)
                        .build());
        assertEquals(0, pageLow.getTotalElements());

        Page<DoctorResponseDto> pageHigh = doctorService.searchDoctors(
                DoctorSearchRequestDto.builder()
                        .endTime(LocalTime.of(16, 0))
                        .page(0)
                        .size(10)
                        .build());
        assertEquals(1, pageHigh.getTotalElements());
    }

    @Test
    void searchDoctors_withStartAndEndTime_filtersOnlyFullyContained() {
        when(authServiceClient.getAllCaregivers()).thenReturn(List.of(sampleCaregiver));
        ScheduleDto sched = ScheduleDto.builder()
                .id(UUID.randomUUID())
                .caregiverId(UUID.fromString(caregiverId))
                .day(DayOfWeek.FRIDAY)
                .startTime(LocalTime.of(8, 0))
                .endTime(LocalTime.of(18, 0))
                .specificDate(LocalDate.now())
                .oneTime(false)
                .build();
        when(konsultasiServiceClient.getSchedulesForCaregivers(List.of(caregiverId)))
                .thenReturn(List.of(sched));
        when(konsultasiServiceClient.getCaregiverSchedules(caregiverId))
                .thenReturn(List.of(sched));

        Page<DoctorResponseDto> pageInside = doctorService.searchDoctors(
                DoctorSearchRequestDto.builder()
                        .startTime(LocalTime.of(9, 0))
                        .endTime(LocalTime.of(17, 0))
                        .page(0)
                        .size(10)
                        .build());
        assertEquals(1, pageInside.getTotalElements());

        Page<DoctorResponseDto> pageOutside = doctorService.searchDoctors(
                DoctorSearchRequestDto.builder()
                        .startTime(LocalTime.of(7, 0))
                        .endTime(LocalTime.of(19, 0))
                        .page(0)
                        .size(10)
                        .build());
        assertEquals(1, pageOutside.getTotalElements());
    }

}
