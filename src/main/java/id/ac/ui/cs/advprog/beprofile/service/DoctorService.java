package id.ac.ui.cs.advprog.beprofile.service;

import id.ac.ui.cs.advprog.beprofile.dto.*;
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
import java.time.format.DateTimeParseException;
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

            caregivers = applyScheduleFiltering(caregivers, searchRequest);

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
        String speciality = searchRequest.getSpeciality();

        if ((name != null && !name.trim().isEmpty()) || (speciality != null && !speciality.trim().isEmpty())) {
            return authServiceClient.searchCaregivers(name, speciality);
        } else {
            return authServiceClient.getAllCaregivers();
        }
    }

    private List<CaregiverDto> applyScheduleFiltering(List<CaregiverDto> caregivers, DoctorSearchRequestDto searchRequest) {

        if (searchRequest.getWorkingDay() != null && !searchRequest.getWorkingDay().trim().isEmpty()) {
            if (searchRequest.getStartTime() != null && !searchRequest.getStartTime().trim().isEmpty() &&
                    searchRequest.getEndTime() != null && !searchRequest.getEndTime().trim().isEmpty()) {

                log.info("Filtering by day '{}' and time range '{}-{}'",
                        searchRequest.getWorkingDay(), searchRequest.getStartTime(), searchRequest.getEndTime());
                return filterCaregiversByDayAndTime(caregivers, searchRequest.getWorkingDay(),
                        searchRequest.getStartTime(), searchRequest.getEndTime());
            } else {
                log.info("Filtering by day '{}' only", searchRequest.getWorkingDay());
                return filterCaregiversByDay(caregivers, searchRequest.getWorkingDay());
            }
        } else if (searchRequest.getWorkingSchedule() != null && !searchRequest.getWorkingSchedule().trim().isEmpty()) {

            log.info("Using deprecated workingSchedule parameter: '{}'", searchRequest.getWorkingSchedule());
            return filterCaregiversByDay(caregivers, searchRequest.getWorkingSchedule());
        }

        return caregivers;
    }

    private List<CaregiverDto> filterCaregiversByDay(List<CaregiverDto> caregivers, String workingDay) {
        try {
            DayOfWeek requestedDay = DayOfWeek.valueOf(workingDay.toUpperCase());

            List<String> caregiverIds = caregivers.stream()
                    .map(CaregiverDto::getId)
                    .collect(Collectors.toList());

            List<ScheduleDto> allSchedules = konsultasiServiceClient.getSchedulesForCaregivers(caregiverIds);

            List<String> matchingCaregiverIds = allSchedules.stream()
                    .filter(schedule -> schedule.getDay().equals(requestedDay))
                    .map(schedule -> schedule.getCaregiverId().toString())
                    .distinct()
                    .collect(Collectors.toList());

            return caregivers.stream()
                    .filter(caregiver -> matchingCaregiverIds.contains(caregiver.getId()))
                    .collect(Collectors.toList());

        } catch (IllegalArgumentException e) {
            log.warn("Invalid working day format: {}. Ignoring day filter.", workingDay);
            return caregivers;
        } catch (Exception e) {
            log.error("Error filtering by day: {}", e.getMessage());
            return caregivers;
        }
    }

    private List<CaregiverDto> filterCaregiversByDayAndTime(List<CaregiverDto> caregivers,
                                                            String workingDay, String startTime, String endTime) {
        try {
            DayOfWeek requestedDay = DayOfWeek.valueOf(workingDay.toUpperCase());
            LocalTime requestedStart = LocalTime.parse(startTime);
            LocalTime requestedEnd = LocalTime.parse(endTime);

            if (!requestedStart.isBefore(requestedEnd)) {
                log.warn("Invalid time range: {} to {}. Start time must be before end time.", startTime, endTime);
                return filterCaregiversByDay(caregivers, workingDay);
            }

            List<String> caregiverIds = caregivers.stream()
                    .map(CaregiverDto::getId)
                    .collect(Collectors.toList());

            List<ScheduleDto> allSchedules = konsultasiServiceClient.getSchedulesForCaregivers(caregiverIds);

            List<String> matchingCaregiverIds = allSchedules.stream()
                    .filter(schedule -> schedule.getDay().equals(requestedDay))
                    .filter(schedule -> timeSlotsOverlap(schedule.getStartTime(), schedule.getEndTime(),
                            requestedStart, requestedEnd))
                    .map(schedule -> schedule.getCaregiverId().toString())
                    .distinct()
                    .collect(Collectors.toList());

            log.info("Found {} doctors available on {} from {} to {}",
                    matchingCaregiverIds.size(), workingDay, startTime, endTime);

            return caregivers.stream()
                    .filter(caregiver -> matchingCaregiverIds.contains(caregiver.getId()))
                    .collect(Collectors.toList());

        } catch (IllegalArgumentException | DateTimeParseException e) {
            log.warn("Invalid day or time format. Day: {}, Start: {}, End: {}. Error: {}",
                    workingDay, startTime, endTime, e.getMessage());
            return filterCaregiversByDay(caregivers, workingDay);
        } catch (Exception e) {
            log.error("Error filtering by day and time: {}", e.getMessage());
            return caregivers;
        }
    }

    private boolean timeSlotsOverlap(LocalTime scheduleStart, LocalTime scheduleEnd,
                                     LocalTime requestedStart, LocalTime requestedEnd) {
        boolean overlaps = requestedStart.isBefore(scheduleEnd) && requestedEnd.isAfter(scheduleStart);

        log.debug("Checking overlap: Schedule({}-{}) vs Requested({}-{}) = {}",
                scheduleStart, scheduleEnd, requestedStart, requestedEnd, overlaps);

        return overlaps;
    }

    private DoctorResponseDto convertCaregiverToDoctorWithSchedules(CaregiverDto caregiver) {
        List<ScheduleDto> schedules = konsultasiServiceClient.getCaregiverSchedules(caregiver.getId());

        CaregiverRatingStatsDto ratingStats = ratingServiceClient.getCaregiverRatingStats(caregiver.getId());

        return DoctorResponseDto.builder()
                .id(caregiver.getId())
                .caregiverId(caregiver.getId())
                .name(caregiver.getName())
                .email(caregiver.getEmail())
                .speciality(caregiver.getSpeciality())
                .workAddress(caregiver.getWorkAddress())
                .phoneNumber(caregiver.getPhoneNumber())
                .description("Dr. " + caregiver.getName() + " specializes in " + caregiver.getSpeciality())
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