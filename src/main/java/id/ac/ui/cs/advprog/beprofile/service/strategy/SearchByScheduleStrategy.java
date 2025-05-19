package id.ac.ui.cs.advprog.beprofile.service.strategy;

import id.ac.ui.cs.advprog.beprofile.model.Doctor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("schedule")
public class SearchByScheduleStrategy implements SearchStrategy {
    @Override
    public List<Doctor> search(List<Doctor> doctors, String criteria) {
        List<Doctor> results = new ArrayList<>();
        if (criteria == null || criteria.trim().isEmpty()) {
            return results;
        }
        String lowerCriteria = criteria.toLowerCase();
        for (Doctor d : doctors) {
            if (d.getWorkSchedule() != null && d.getWorkSchedule().toLowerCase().contains(lowerCriteria)) {
                results.add(d);
            }
        }
        return results;
    }
}