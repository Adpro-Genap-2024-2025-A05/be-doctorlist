package id.ac.ui.cs.advprog.beprofile.service.strategy;

import id.ac.ui.cs.advprog.beprofile.model.Role;
import org.springframework.stereotype.Component;

@Component
public class PacillianAuthorizationStrategy implements AuthorizationStrategy {

    @Override
    public boolean authorize(Role role, String resourceUserId, String tokenUserId) {
        // Validasi bahwa role adalah PACILLIAN
        if (role != Role.PACILLIANS) {
            throw new IllegalArgumentException("Invalid role for PacillianAuthorizationStrategy: " + role);
        }

        // Pacillian hanya dapat mengakses data mereka sendiri
        return resourceUserId.equals(tokenUserId);
    }
}