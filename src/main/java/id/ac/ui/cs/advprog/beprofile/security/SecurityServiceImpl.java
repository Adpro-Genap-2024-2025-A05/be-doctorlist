package id.ac.ui.cs.advprog.beprofile.security;

import id.ac.ui.cs.advprog.beprofile.model.Role;
import id.ac.ui.cs.advprog.beprofile.model.User;
import org.springframework.stereotype.Service;

@Service
public class SecurityServiceImpl implements SecurityService {

    @Override
    public boolean authenticate(String token) {
        return token != null && token.startsWith("Bearer ");
    }

    @Override
    public boolean authorize(String resourceUserId, String tokenUserId) {
        User user = getUserFromToken("Bearer " + tokenUserId); // Dummy decoding
        Role role = user.getRole();

        // Strategi otorisasi berdasarkan peran
        if (role == Role.PACILLIANS) {
            return resourceUserId.equals(tokenUserId); // Pacillian hanya dapat mengakses data mereka sendiri
        } else if (role == Role.CAREGIVER) {
            return resourceUserId.equals(tokenUserId); // Dokter hanya dapat mengakses data mereka sendiri
        }

        throw new IllegalArgumentException("Unsupported role: " + role);
    }

    @Override
    public User getUserFromToken(String token) {
        User user = new User();
        user.setId("mockUserId");
        user.setEmail("mock@example.com");
        user.setFullname("Mock User");
        user.setRole(Role.PACILLIANS); // Default role for testing
        return user;
    }
}