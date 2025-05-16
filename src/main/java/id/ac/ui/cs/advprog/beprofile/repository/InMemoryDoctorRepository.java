package id.ac.ui.cs.advprog.beprofile.repository;

import id.ac.ui.cs.advprog.beprofile.model.Doctor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@Repository
public class InMemoryDoctorRepository implements DoctorRepository {

    private final List<Doctor> doctorData = new ArrayList<>();

    @Override
    public Doctor create(Doctor doctor) {
        if (doctor.getId() == null || doctor.getId().trim().isEmpty()) {
            doctor.setId(UUID.randomUUID().toString());
        }
        doctorData.add(doctor);
        return doctor;
    }

    @Override
    public Iterator<Doctor> findAll() {
        return new ArrayList<>(doctorData).iterator();
    }

    @Override
    public Doctor findById(String id) {
        return doctorData.stream()
                .filter(doctor -> doctor.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Doctor update(String id, Doctor updatedDoctor) {
        for (int i = 0; i < doctorData.size(); i++) {
            if (doctorData.get(i).getId().equals(id)) {
                doctorData.set(i, updatedDoctor);
                return updatedDoctor;
            }
        }
        return null;
    }

    @Override
    public Doctor delete(String id) {
        Doctor doctor = findById(id);
        if (doctor != null) {
            doctorData.removeIf(d -> d.getId().equals(id));
        }
        return doctor;
    }

    public void clear() {
        doctorData.clear();
    }
}
