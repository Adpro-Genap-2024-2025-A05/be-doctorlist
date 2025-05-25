package id.ac.ui.cs.advprog.beprofile.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleDto {
    private UUID id;
    private UUID caregiverId;
    private DayOfWeek day;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDate specificDate;
    private boolean oneTime;
}
