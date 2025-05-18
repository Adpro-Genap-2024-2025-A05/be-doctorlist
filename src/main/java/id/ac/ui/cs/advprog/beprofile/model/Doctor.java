package id.ac.ui.cs.advprog.beprofile.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Doctor {
    private String id;
    private String name;
    private String practiceAddress;
    private String workSchedule;
    private String email;
    private String phoneNumber;
    private double rating;
    private String speciality;
}