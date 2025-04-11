package id.ac.ui.cs.advprog.beprofile.security;

import id.ac.ui.cs.advprog.beprofile.model.User;

public interface AuthenticationService {
    User authenticate(String username, String password);
}
