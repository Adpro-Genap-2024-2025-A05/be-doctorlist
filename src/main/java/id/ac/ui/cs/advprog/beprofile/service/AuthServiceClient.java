package id.ac.ui.cs.advprog.beprofile.service;

import id.ac.ui.cs.advprog.beprofile.dto.ApiResponseDto;
import id.ac.ui.cs.advprog.beprofile.dto.CaregiverDto;
import id.ac.ui.cs.advprog.beprofile.enums.Speciality;
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
public class AuthServiceClient {

    private final RestTemplate restTemplate;

    @Value("${auth.service.base-url}")
    private String authServiceBaseUrl;

    public List<CaregiverDto> getAllCaregivers() {
        try {
            String url = authServiceBaseUrl + "/data";
            
            ResponseEntity<ApiResponseDto<List<CaregiverDto>>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<ApiResponseDto<List<CaregiverDto>>>() {}
            );

            if (response.getBody() != null && response.getBody().getData() != null) {
                log.info("Successfully fetched {} caregivers from auth service", 
                        response.getBody().getData().size());
                return response.getBody().getData();
            }
            
            log.warn("No caregivers found from auth service");
            return List.of();
            
        } catch (Exception e) {
            log.error("Error fetching caregivers from auth service: {}", e.getMessage());
            throw new RuntimeException("Failed to fetch caregivers from auth service", e);
        }
    }

    public List<CaregiverDto> searchCaregivers(String name, Speciality speciality) { 
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(authServiceBaseUrl + "/data/search");
            
            if (name != null && !name.trim().isEmpty()) {
                builder.queryParam("name", name);
            }
            
            if (speciality != null) {
                builder.queryParam("speciality", speciality.getDisplayName());
            }
            
            String url = builder.toUriString();
            
            ResponseEntity<ApiResponseDto<List<CaregiverDto>>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<ApiResponseDto<List<CaregiverDto>>>() {}
            );

            if (response.getBody() != null && response.getBody().getData() != null) {
                log.info("Successfully searched caregivers: found {} results", 
                        response.getBody().getData().size());
                return response.getBody().getData();
            }
            
            return List.of();
            
        } catch (Exception e) {
            log.error("Error searching caregivers from auth service: {}", e.getMessage());
            throw new RuntimeException("Failed to search caregivers from auth service", e);
        }
    }

    public CaregiverDto getCaregiverById(String id) {
        try {
            String url = authServiceBaseUrl + "/data/" + id;
            
            ResponseEntity<ApiResponseDto<CaregiverDto>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<ApiResponseDto<CaregiverDto>>() {}
            );

            if (response.getBody() != null && response.getBody().getData() != null) {
                return response.getBody().getData();
            }
            
            throw new RuntimeException("Caregiver not found with id: " + id);
            
        } catch (Exception e) {
            log.error("Error fetching caregiver by id from auth service: {}", e.getMessage());
            throw new RuntimeException("Failed to fetch caregiver by id from auth service", e);
        }
    }
}