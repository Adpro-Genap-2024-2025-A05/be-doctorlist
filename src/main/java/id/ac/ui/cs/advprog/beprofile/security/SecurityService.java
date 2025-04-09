package id.ac.ui.cs.advprog.beprofile.security;

import id.ac.ui.cs.advprog.beprofile.model.User;

public interface SecurityService {
    boolean authenticate(String email, String password);
    boolean authorize(User user, String action);
}
