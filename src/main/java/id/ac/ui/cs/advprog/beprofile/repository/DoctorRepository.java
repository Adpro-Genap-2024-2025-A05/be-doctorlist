package id.ac.ui.cs.advprog.beprofile.repository;

import id.ac.ui.cs.advprog.beprofile.model.Doctor;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorRepository extends WriteRepository<Doctor>, ReadRepository<Doctor> {
}
