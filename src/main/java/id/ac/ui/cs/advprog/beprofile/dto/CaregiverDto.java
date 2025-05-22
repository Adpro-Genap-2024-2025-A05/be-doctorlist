package id.ac.ui.cs.advprog.beprofile.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CaregiverDto {
    private String id;
    private String email;
    private String name;
    private String nik;
    private String address;
    private String phoneNumber;
    private String role;
    private String speciality;
    private String workAddress;
}