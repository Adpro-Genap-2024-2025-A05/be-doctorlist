package id.ac.ui.cs.advprog.beprofile.service.strategy;

import id.ac.ui.cs.advprog.beprofile.model.Role;
import org.springframework.stereotype.Component;

@Component
public class DoctorAuthorizationStrategy implements AuthorizationStrategy {

    @Override
    public boolean authorize(Role role, String resourceUserId, String tokenUserId) {
        // Validasi bahwa role adalah DOCTOR
        if (role != Role.CAREGIVER) {
            throw new IllegalArgumentException("Invalid role for DoctorAuthorizationStrategy: " + role);
        }

        // Dokter hanya dapat mengakses data mereka sendiri
        return resourceUserId.equals(tokenUserId);
    }
}