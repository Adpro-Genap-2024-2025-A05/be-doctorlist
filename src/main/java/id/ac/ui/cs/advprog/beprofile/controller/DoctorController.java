package id.ac.ui.cs.advprog.beprofile.controller;

import id.ac.ui.cs.advprog.beprofile.model.Doctor;
import id.ac.ui.cs.advprog.beprofile.services.DoctorSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    private final DoctorSearchService doctorSearchService;

    @Autowired
    public DoctorController(DoctorSearchService doctorSearchService) {
        this.doctorSearchService = doctorSearchService;
    }

    @GetMapping
    public List<Doctor> getDoctors(@RequestParam(name = "searchType", defaultValue = "name") String searchType,
                                   @RequestParam(name = "criteria", defaultValue = "") String criteria) {
        return doctorSearchService.search(criteria, searchType);
    }

    @GetMapping("/{id}")
    public Doctor getDoctorById(@PathVariable String id) {
        return doctorSearchService.getDoctorById(id);
    }
}
