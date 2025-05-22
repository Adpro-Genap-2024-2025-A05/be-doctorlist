package id.ac.ui.cs.advprog.beprofile.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DoctorSearchRequestDto {
    private String name;
    private String speciality;
    private String workingSchedule; // Otw implementasi
    private Integer page;
    private Integer size;
}