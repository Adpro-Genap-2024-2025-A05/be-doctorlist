package id.ac.ui.cs.advprog.beprofile.dto;

import id.ac.ui.cs.advprog.beprofile.enums.Speciality;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DoctorSearchRequestDto {
    private String name;
    private Speciality speciality;
    private String workingSchedule;
    private DayOfWeek workingDay;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer page;
    private Integer size;
}