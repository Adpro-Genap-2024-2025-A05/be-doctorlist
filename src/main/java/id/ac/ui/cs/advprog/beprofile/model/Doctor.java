package id.ac.ui.cs.advprog.beprofile.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "doctors")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Doctor {

    @Id
    @Column(length = 36)
    private String id;
    private String name;
    private String practiceAddress;
    private String workSchedule;
    private String email;
    private String phoneNumber;
    private double rating;
    private String speciality;
}