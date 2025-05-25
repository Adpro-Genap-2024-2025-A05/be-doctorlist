package id.ac.ui.cs.advprog.beprofile.service;

import id.ac.ui.cs.advprog.beprofile.dto.ApiResponseDto;
import id.ac.ui.cs.advprog.beprofile.dto.CaregiverRatingStatsDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RatingServiceClientTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private RatingServiceClient client;

    private String baseUrl;
    private String caregiverId;

    @BeforeEach
    void setUp() {
        baseUrl = "http://rating.example.com";
        ReflectionTestUtils.setField(client, "ratingServiceBaseUrl", baseUrl);
        caregiverId = UUID.randomUUID().toString();
    }

    @Test
    void whenRemoteReturnsData_thenReturnsThatData() {
        CaregiverRatingStatsDto stats = CaregiverRatingStatsDto.builder()
                .averageRating(4.2)
                .totalRatings(17L)
                .build();

        ApiResponseDto<CaregiverRatingStatsDto> wrapper = new ApiResponseDto<>();
        wrapper.setData(stats);

        ResponseEntity<ApiResponseDto<CaregiverRatingStatsDto>> respEntity =
                ResponseEntity.ok(wrapper);

        given(restTemplate.exchange(
                eq(baseUrl + "/rating/caregiver/" + caregiverId + "/stats"),
                eq(HttpMethod.GET),
                isNull(),
                any(ParameterizedTypeReference.class)
        )).willReturn(respEntity);

        CaregiverRatingStatsDto result = client.getCaregiverRatingStats(caregiverId);

        assertThat(result).isSameAs(stats);
        verify(restTemplate).exchange(
                anyString(), any(HttpMethod.class), isNull(), any(ParameterizedTypeReference.class)
        );
    }

    @Test
    void whenRemoteReturnsNullBody_thenReturnsDefaultStats() {
        ResponseEntity<ApiResponseDto<CaregiverRatingStatsDto>> respEntity =
                ResponseEntity.ok(null);

        given(restTemplate.exchange(anyString(), any(), any(), any(ParameterizedTypeReference.class)))
                .willReturn(respEntity);

        CaregiverRatingStatsDto result = client.getCaregiverRatingStats(caregiverId);

        assertThat(result.getAverageRating()).isEqualTo(0.0);
        assertThat(result.getTotalRatings()).isZero();
    }

    @Test
    void whenRemoteThrows_thenReturnsDefaultStats() {
        given(restTemplate.exchange(anyString(), any(), any(), any(ParameterizedTypeReference.class)))
                .willThrow(new RuntimeException("service down"));

        CaregiverRatingStatsDto result = client.getCaregiverRatingStats(caregiverId);

        assertThat(result.getAverageRating()).isEqualTo(0.0);
        assertThat(result.getTotalRatings()).isZero();
    }
}
