package id.ac.ui.cs.advprog.beprofile.dto;

import id.ac.ui.cs.advprog.beprofile.enums.Speciality;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DoctorResponseDto {
    private String id;
    private String name;
    private String email;
    private Speciality speciality;  
    private String workAddress;
    private String phoneNumber;
    private String description;
    private Double rating;
    private Integer totalReviews;
    private List<ScheduleDto> workingSchedules;
}