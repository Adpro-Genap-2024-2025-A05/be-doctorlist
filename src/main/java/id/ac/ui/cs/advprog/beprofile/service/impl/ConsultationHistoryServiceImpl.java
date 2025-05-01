package id.ac.ui.cs.advprog.beprofile.service.impl;

import id.ac.ui.cs.advprog.beprofile.model.ConsultationHistory;
import id.ac.ui.cs.advprog.beprofile.repository.ConsultationHistoryRepository;
import id.ac.ui.cs.advprog.beprofile.service.ConsultationHistoryService; // Pastikan import ini ada
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsultationHistoryServiceImpl implements ConsultationHistoryService {

    @Autowired
    private ConsultationHistoryRepository consultationHistoryRepository;

    @Override
    public List<ConsultationHistory> getConsultationHistory(String userId) {
        return consultationHistoryRepository.findByUserId(userId);
    }
}