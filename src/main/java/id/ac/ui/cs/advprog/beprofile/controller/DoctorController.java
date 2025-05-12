package id.ac.ui.cs.advprog.beprofile.controller;

import id.ac.ui.cs.advprog.beprofile.model.Doctor;
import id.ac.ui.cs.advprog.beprofile.service.DoctorSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/doctors")
public class DoctorController {
    private final DoctorSearchService doctorSearchService;

    @Autowired
    public DoctorController(DoctorSearchService doctorSearchService) {
        this.doctorSearchService = doctorSearchService;
    }

    @GetMapping
    public ResponseEntity<List<Doctor>> getDoctors(
            @RequestParam(name = "searchType", defaultValue = "name") String searchType,
            @RequestParam(name = "criteria", defaultValue = "") String criteria) {
        List<Doctor> doctors = doctorSearchService.search(criteria, searchType);
        return ResponseEntity.ok(doctors);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Doctor> getDoctorById(@PathVariable String id) {
        Doctor doctor = doctorSearchService.getDoctorById(id);
        if (doctor != null) {
            return ResponseEntity.ok(doctor);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}