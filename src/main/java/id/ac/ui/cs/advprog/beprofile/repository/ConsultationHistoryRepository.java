package id.ac.ui.cs.advprog.beprofile.repository;

import id.ac.ui.cs.advprog.beprofile.model.ConsultationHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConsultationHistoryRepository extends JpaRepository<ConsultationHistory, Long> {
    List<ConsultationHistory> findByUserId(String userId);
}