package id.ac.ui.cs.advprog.beprofile.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CaregiverRatingStatsDto {
    private UUID caregiverId;
    private Double averageRating;
    private Long totalRatings; 
}