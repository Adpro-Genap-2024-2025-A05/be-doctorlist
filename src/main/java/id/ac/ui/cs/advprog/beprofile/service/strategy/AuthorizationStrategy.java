package id.ac.ui.cs.advprog.beprofile.service.strategy;

import id.ac.ui.cs.advprog.beprofile.model.Role;

public interface AuthorizationStrategy {
    boolean authorize(Role role, String resourceUserId, String tokenUserId);
}