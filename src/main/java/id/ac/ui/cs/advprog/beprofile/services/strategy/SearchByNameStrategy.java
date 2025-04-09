package id.ac.ui.cs.advprog.beprofile.services.strategy;


import id.ac.ui.cs.advprog.beprofile.model.Doctor;

import java.util.ArrayList;
import java.util.List;

public class SearchByNameStrategy implements SearchStrategy {

    @Override
    public List<Doctor> search(List<Doctor> doctors, String criteria) {
        List<Doctor> results = new ArrayList<>();
        if (criteria == null || criteria.trim().isEmpty()) {
            return results;
        }
        // Case-insensitive search.
        String lowerCriteria = criteria.toLowerCase();
        for (Doctor doctor : doctors) {
            if (doctor.getName() != null && doctor.getName().toLowerCase().contains(lowerCriteria)) {
                results.add(doctor);
            }
        }
        return results;
    }
}