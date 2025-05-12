package id.ac.ui.cs.advprog.beprofile.service.strategy;

import id.ac.ui.cs.advprog.beprofile.model.Doctor;

import java.util.List;

public interface SearchStrategy {
    List<Doctor> search(List<Doctor> doctors, String criteria);
}