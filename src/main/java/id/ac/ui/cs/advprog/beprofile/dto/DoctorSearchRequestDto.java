package id.ac.ui.cs.advprog.beprofile.dto;

import id.ac.ui.cs.advprog.beprofile.enums.Speciality;
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
<<<<<<< HEAD
    private Speciality speciality;  
    private String workingSchedule; 
=======
    private String speciality;
    private String workingSchedule;
    private String workingDay;
    private String startTime;
    private String endTime;
>>>>>>> c4dc56d02cdfd643ae14f41617ae1bda6a05400d
    private Integer page;
    private Integer size;
}