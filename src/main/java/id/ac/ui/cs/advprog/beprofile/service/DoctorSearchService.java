package id.ac.ui.cs.advprog.beprofile.service;

import id.ac.ui.cs.advprog.beprofile.model.Doctor;
import id.ac.ui.cs.advprog.beprofile.repository.DoctorRepository;
import id.ac.ui.cs.advprog.beprofile.service.strategy.SearchStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import id.ac.ui.cs.advprog.beprofile.exception.ResourceNotFoundException;

import java.util.List;
import java.util.Map;

@Service
public class DoctorSearchService {

    private final DoctorRepository doctorRepository;
    private final Map<String, SearchStrategy> strategies;

    @Autowired
    public DoctorSearchService(DoctorRepository doctorRepository,
                               Map<String, SearchStrategy> strategies) {
        this.doctorRepository = doctorRepository;
        this.strategies       = strategies;
    }

    public List<Doctor> search(String criteria, String type) {
        var strategy = strategies.get(type);
        if (strategy == null) {
            throw new IllegalArgumentException("Search type " + type + " is not supported.");
        }
        var all = doctorRepository.findAll();
        return strategy.search(all, criteria);
    }

    public Doctor getDoctorById(String id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found: " + id));
    }
}
