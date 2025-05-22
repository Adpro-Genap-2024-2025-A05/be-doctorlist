package id.ac.ui.cs.advprog.beprofile.service;

import id.ac.ui.cs.advprog.beprofile.dto.ApiResponseDto;
import id.ac.ui.cs.advprog.beprofile.dto.ScheduleDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class KonsultasiServiceClient {

    private final RestTemplate restTemplate;

    @Value("${konsultasi.service.base-url}")
    private String konsultasiServiceBaseUrl;

    public List<ScheduleDto> getCaregiverSchedules(String caregiverId) {
        try {
            String url = konsultasiServiceBaseUrl + "/data/caregiver/" + caregiverId;
            
            ResponseEntity<ApiResponseDto<List<ScheduleDto>>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<ApiResponseDto<List<ScheduleDto>>>() {}
            );

            if (response.getBody() != null && response.getBody().getData() != null) {
                log.info("Successfully fetched {} schedules for caregiver {}", 
                        response.getBody().getData().size(), caregiverId);
                return response.getBody().getData();
            }
            
            log.warn("No schedules found for caregiver {}", caregiverId);
            return List.of();
            
        } catch (Exception e) {
            log.error("Error fetching schedules for caregiver {} from konsultasi service: {}", 
                     caregiverId, e.getMessage());
            return List.of();
        }
    }

    public List<ScheduleDto> getAvailableSchedules(List<String> caregiverIds) {
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(
                konsultasiServiceBaseUrl + "/data/available");
            
            if (caregiverIds != null && !caregiverIds.isEmpty()) {
                builder.queryParam("caregiverIds", caregiverIds.toArray());
            }
            
            String url = builder.toUriString();
            
            ResponseEntity<ApiResponseDto<List<ScheduleDto>>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<ApiResponseDto<List<ScheduleDto>>>() {}
            );

            if (response.getBody() != null && response.getBody().getData() != null) {
                log.info("Successfully fetched {} available schedules for {} caregivers", 
                        response.getBody().getData().size(), caregiverIds.size());
                return response.getBody().getData();
            }
            
            log.warn("No available schedules found for caregivers: {}", caregiverIds);
            return List.of();
            
        } catch (Exception e) {
            log.error("Error fetching available schedules from konsultasi service: {}", e.getMessage());
            return caregiverIds.stream()
                    .flatMap(id -> getCaregiverSchedules(id).stream())
                    .toList();
        }
    }
}