package id.ac.ui.cs.advprog.beprofile.service.strategy;

import id.ac.ui.cs.advprog.beprofile.model.Doctor;

import java.util.List;
import java.util.stream.Collectors;

public class SearchByNameStrategy implements SearchStrategy {
    @Override
    public List<Doctor> search(List<Doctor> doctors, String criteria) {
        if (criteria == null || criteria.isEmpty()) {
            return doctors;
        }
        
        String lowerCaseCriteria = criteria.toLowerCase();
        return doctors.stream()
                .filter(doctor -> doctor.getName() != null && 
                        doctor.getName().toLowerCase().contains(lowerCaseCriteria))
                .collect(Collectors.toList());
    }
}