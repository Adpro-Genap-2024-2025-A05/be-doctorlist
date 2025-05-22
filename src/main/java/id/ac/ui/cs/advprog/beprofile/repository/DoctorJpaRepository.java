package id.ac.ui.cs.advprog.beprofile.repository;

import id.ac.ui.cs.advprog.beprofile.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorJpaRepository
        extends JpaRepository<Doctor, String> {
}
