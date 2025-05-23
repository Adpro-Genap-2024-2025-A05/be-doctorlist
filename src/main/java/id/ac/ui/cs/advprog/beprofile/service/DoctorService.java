package id.ac.ui.cs.advprog.beprofile.service;

import id.ac.ui.cs.advprog.beprofile.dto.*;
import id.ac.ui.cs.advprog.beprofile.enums.Speciality;
import id.ac.ui.cs.advprog.beprofile.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DoctorService {

    private final AuthServiceClient authServiceClient;
    private final KonsultasiServiceClient konsultasiServiceClient;
    private final RatingServiceClient ratingServiceClient;

    public Page<DoctorResponseDto> searchDoctors(DoctorSearchRequestDto searchRequest) {
        log.info("Searching doctors with criteria: {}", searchRequest);

        try {
            List<CaregiverDto> caregivers = fetchCaregiversFromAuth(searchRequest);

            if (hasScheduleFilters(searchRequest)) {
                caregivers = filterCaregiversBySchedule(caregivers, searchRequest);
            }

            List<DoctorResponseDto> doctors = caregivers.stream()
                    .map(this::convertCaregiverToDoctorWithSchedules)
                    .collect(Collectors.toList());

            Pageable pageable = createPageable(searchRequest.getPage(), searchRequest.getSize());
            return createPageFromList(doctors, pageable);

        } catch (Exception e) {
            log.error("Error searching doctors: {}", e.getMessage());
            throw new RuntimeException("Failed to search doctors", e);
        }
    }

    public DoctorResponseDto getDoctorById(String caregiverId) {
        log.info("Fetching doctor with caregiver ID: {}", caregiverId);

        try {
            CaregiverDto caregiver = authServiceClient.getCaregiverById(caregiverId);
            return convertCaregiverToDoctorWithSchedules(caregiver);

        } catch (Exception e) {
            log.error("Error fetching doctor by ID: {}", e.getMessage());
            throw new ResourceNotFoundException("Doctor not found with ID: " + caregiverId);
        }
    }

    private List<CaregiverDto> fetchCaregiversFromAuth(DoctorSearchRequestDto searchRequest) {
        String name = searchRequest.getName();
        Speciality speciality = searchRequest.getSpeciality();

        if ((name != null && !name.trim().isEmpty()) || speciality != null) {
            return authServiceClient.searchCaregivers(name, speciality);
        } else {
            return authServiceClient.getAllCaregivers();
        }
    }

    private boolean hasScheduleFilters(DoctorSearchRequestDto searchRequest) {
        return (searchRequest.getWorkingSchedule() != null && !searchRequest.getWorkingSchedule().trim().isEmpty()) ||
                searchRequest.getWorkingDay() != null ||
                searchRequest.getStartTime() != null ||
                searchRequest.getEndTime() != null;
    }

    private List<CaregiverDto> filterCaregiversBySchedule(List<CaregiverDto> caregivers, DoctorSearchRequestDto searchRequest) {
        try {
            List<String> caregiverIds = caregivers.stream()
                    .map(CaregiverDto::getId)
                    .collect(Collectors.toList());

            List<ScheduleDto> allSchedules = konsultasiServiceClient.getSchedulesForCaregivers(caregiverIds);

            List<ScheduleDto> filteredSchedules = allSchedules.stream()
                    .filter(schedule -> matchesScheduleCriteria(schedule, searchRequest))
                    .collect(Collectors.toList());

            List<String> matchingCaregiverIds = filteredSchedules.stream()
                    .map(schedule -> schedule.getCaregiverId().toString())
                    .distinct()
                    .collect(Collectors.toList());

            return caregivers.stream()
                    .filter(caregiver -> matchingCaregiverIds.contains(caregiver.getId()))
                    .collect(Collectors.toList());

        } catch (Exception e) {
            log.error("Error filtering by schedule: {}", e.getMessage());
            return caregivers;
        }
    }

    private boolean matchesScheduleCriteria(ScheduleDto schedule, DoctorSearchRequestDto searchRequest) {

        if (searchRequest.getWorkingSchedule() != null && !searchRequest.getWorkingSchedule().trim().isEmpty()) {
            try {
                DayOfWeek requestedDay = DayOfWeek.valueOf(searchRequest.getWorkingSchedule().toUpperCase());
                if (!schedule.getDay().equals(requestedDay)) {
                    return false;
                }
            } catch (IllegalArgumentException e) {
                log.warn("Invalid working schedule format: {}. Ignoring legacy schedule filter.", searchRequest.getWorkingSchedule());
            }
        }

        if (searchRequest.getWorkingDay() != null) {
            if (!schedule.getDay().equals(searchRequest.getWorkingDay())) {
                return false;
            }
        }

        if (searchRequest.getStartTime() != null || searchRequest.getEndTime() != null) {
            LocalTime scheduleStart = schedule.getStartTime();
            LocalTime scheduleEnd = schedule.getEndTime();

            LocalTime requestStart = searchRequest.getStartTime();
            LocalTime requestEnd = searchRequest.getEndTime();

            if (requestStart != null && requestEnd == null) {

                if (requestStart.isBefore(scheduleStart) || requestStart.isAfter(scheduleEnd) || requestStart.equals(scheduleEnd)) {
                    return false;
                }
            }

            else if (requestStart == null && requestEnd != null) {

                if (requestEnd.isBefore(scheduleStart) || requestEnd.isAfter(scheduleEnd) || requestEnd.equals(scheduleStart)) {
                    return false;
                }
            }

            else if (requestStart != null && requestEnd != null) {

                if (requestEnd.isBefore(scheduleStart) || requestEnd.equals(scheduleStart) ||
                        requestStart.isAfter(scheduleEnd) || requestStart.equals(scheduleEnd)) {
                    return false;
                }
            }
        }

        return true;
    }

    private DoctorResponseDto convertCaregiverToDoctorWithSchedules(CaregiverDto caregiver) {
        List<ScheduleDto> schedules = konsultasiServiceClient.getCaregiverSchedules(caregiver.getId());

        CaregiverRatingStatsDto ratingStats = ratingServiceClient.getCaregiverRatingStats(caregiver.getId());

        return DoctorResponseDto.builder()
                .id(caregiver.getId())
                .name(caregiver.getName())
                .email(caregiver.getEmail())
                .speciality(caregiver.getSpeciality())
                .workAddress(caregiver.getWorkAddress())
                .phoneNumber(caregiver.getPhoneNumber())
                .description("Dr. " + caregiver.getName() + " specializes in " + caregiver.getSpeciality().getDisplayName())
                .rating(ratingStats.getAverageRating())
                .totalReviews(ratingStats.getTotalReviews())
                .workingSchedules(schedules)
                .build();
    }

    private Pageable createPageable(Integer page, Integer size) {
        int pageNumber = page != null && page >= 0 ? page : 0;
        int pageSize = size != null && size > 0 && size <= 100 ? size : 10;

        return PageRequest.of(pageNumber, pageSize, Sort.by("name").ascending());
    }

    private Page<DoctorResponseDto> createPageFromList(List<DoctorResponseDto> doctors, Pageable pageable) {
        int start = Math.min((int) pageable.getOffset(), doctors.size());
        int end = Math.min((start + pageable.getPageSize()), doctors.size());

        List<DoctorResponseDto> pageContent = doctors.subList(start, end);

        return new PageImpl<>(pageContent, pageable, doctors.size());
    }
}