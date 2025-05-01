package id.ac.ui.cs.advprog.beprofile.service;

import id.ac.ui.cs.advprog.beprofile.model.ConsultationHistory;

import java.util.List;

public interface ConsultationHistoryService {
    List<ConsultationHistory> getConsultationHistory(String userId);
}