package id.ac.ui.cs.advprog.beprofile.security;

import id.ac.ui.cs.advprog.beprofile.model.User;

public interface SecurityService {
    boolean authenticate(String token); // cocok dengan controller
    boolean authorize(String resourceUserId, String tokenUserId); // cocok juga
    User getUserFromToken(String token); // untuk extract info user dari token
}
