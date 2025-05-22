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
    private String workingSchedule;
    private String workingDay;
    private String startTime;
    private String endTime;
    private Integer page;
    private Integer size;
}