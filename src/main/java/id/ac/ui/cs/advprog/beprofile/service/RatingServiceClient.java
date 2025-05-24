package id.ac.ui.cs.advprog.beprofile.service;

import id.ac.ui.cs.advprog.beprofile.dto.ApiResponseDto;
import id.ac.ui.cs.advprog.beprofile.dto.CaregiverRatingStatsDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RatingServiceClient {

    private final RestTemplate restTemplate;

    @Value("${rating.service.base-url}")
    private String ratingServiceBaseUrl;

    public CaregiverRatingStatsDto getCaregiverRatingStats(String caregiverId) {
        try {
            String url = ratingServiceBaseUrl + "/rating/caregiver/" + caregiverId + "/stats";
            log.info("Calling rating service at URL: {}", url);

            ResponseEntity<ApiResponseDto<CaregiverRatingStatsDto>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<ApiResponseDto<CaregiverRatingStatsDto>>() {
                    });

            log.info("Response status: {}", response.getStatusCode());
            log.info("Response body: {}", response.getBody());

            if (response.getBody() != null && response.getBody().getData() != null) {
                CaregiverRatingStatsDto stats = response.getBody().getData();

                return stats;
            }

            log.warn("No rating stats found for caregiver {}", caregiverId);
            return createDefaultStats();

        } catch (Exception e) {
            log.error("Error fetching rating stats for caregiver {} from rating service: {}",
                    caregiverId, e.getMessage(), e);
            return createDefaultStats();
        }
    }

    private CaregiverRatingStatsDto createDefaultStats() {
        return CaregiverRatingStatsDto.builder()
                .averageRating(0.0)
                .totalRatings(0L)
                .build();
    }
}