package id.ac.ui.cs.advprog.beprofile.security;

import id.ac.ui.cs.advprog.beprofile.model.User;
import id.ac.ui.cs.advprog.beprofile.model.Role;
import org.springframework.stereotype.Service;

@Service
public class SecurityServiceImpl implements SecurityService {

    @Override
    public boolean authenticate(String token) {
        // Dummy check: token tidak null dan ada awalan Bearer
        return token != null && token.startsWith("Bearer ");
    }

    @Override
    public boolean authorize(String resourceUserId, String tokenUserId) {
        // Dummy check: hanya boleh akses data sendiri
        return resourceUserId.equals(tokenUserId);
    }

    @Override
    public User getUserFromToken(String token) {
        // Dummy decode token jadi user
        User user = new User();
        user.setId("mockUserId");
        user.setEmail("mock@example.com");
        user.setFullname("Mock User");
        user.setRole(Role.CAREGIVER); // atau PACILLIANS
        return user;
    }
}
