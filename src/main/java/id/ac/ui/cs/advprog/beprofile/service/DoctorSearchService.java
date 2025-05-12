package id.ac.ui.cs.advprog.beprofile.service;

import id.ac.ui.cs.advprog.beprofile.model.Doctor;
import id.ac.ui.cs.advprog.beprofile.service.strategy.SearchByNameStrategy;
import id.ac.ui.cs.advprog.beprofile.service.strategy.SearchStrategy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DoctorSearchService {

    private final List<Doctor> doctors = new ArrayList<>();

    private final Map<String, SearchStrategy> strategies;

    public DoctorSearchService() {
        strategies = new HashMap<>();
        strategies.put("name", new SearchByNameStrategy());
    }

    public void addDoctor(Doctor doctor) {
        if (doctor != null) {
            doctors.add(doctor);
        }
    }

    public void clearDoctors() {
        doctors.clear();
    }

    public List<Doctor> search(String criteria, String type) {
        SearchStrategy strategy = strategies.get(type);
        if (strategy == null) {
            throw new IllegalArgumentException("Search type " + type + " is not supported.");
        }
        return strategy.search(doctors, criteria);
    }

    public Doctor getDoctorById(String id) {
        return doctors.stream()
                .filter(doctor -> doctor.getId() != null && doctor.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}