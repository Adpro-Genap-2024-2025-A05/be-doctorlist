package id.ac.ui.cs.advprog.beprofile.controller;

import id.ac.ui.cs.advprog.beprofile.dto.ApiResponseDto;
import id.ac.ui.cs.advprog.beprofile.dto.DoctorResponseDto;
import id.ac.ui.cs.advprog.beprofile.dto.DoctorSearchRequestDto;
import id.ac.ui.cs.advprog.beprofile.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/doctor")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DoctorController {

    private final DoctorService doctorService;

    @GetMapping(path = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseDto<Page<DoctorResponseDto>>> searchDoctors(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String speciality,
            @RequestParam(required = false) String workingSchedule,
            @RequestParam(required = false) String workingDay,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {

        DoctorSearchRequestDto searchRequest = DoctorSearchRequestDto.builder()
                .name(name)
                .speciality(speciality)
                .workingSchedule(workingSchedule)
                .workingDay(workingDay)
                .startTime(startTime)
                .endTime(endTime)
                .page(page)
                .size(size)
                .build();

        Page<DoctorResponseDto> doctors = doctorService.searchDoctors(searchRequest);

        return ResponseEntity.ok(
                ApiResponseDto.success(
                        HttpStatus.OK.value(),
                        "Doctors retrieved successfully",
                        doctors
                )
        );
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseDto<Page<DoctorResponseDto>>> getAllDoctors(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {

        DoctorSearchRequestDto searchRequest = DoctorSearchRequestDto.builder()
                .page(page)
                .size(size)
                .build();

        Page<DoctorResponseDto> doctors = doctorService.searchDoctors(searchRequest);

        return ResponseEntity.ok(
                ApiResponseDto.success(
                        HttpStatus.OK.value(),
                        "All doctors retrieved successfully",
                        doctors
                )
        );
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseDto<DoctorResponseDto>> getDoctorById(@PathVariable String id) {
        DoctorResponseDto doctor = doctorService.getDoctorById(id);

        return ResponseEntity.ok(
                ApiResponseDto.success(
                        HttpStatus.OK.value(),
                        "Doctor details retrieved successfully",
                        doctor
                )
        );
    }
}