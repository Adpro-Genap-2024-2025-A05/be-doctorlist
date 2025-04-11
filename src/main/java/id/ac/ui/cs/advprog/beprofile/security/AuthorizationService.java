package id.ac.ui.cs.advprog.beprofile.security;

import id.ac.ui.cs.advprog.beprofile.model.User;

public interface AuthorizationService {
    boolean authorize(User user, String action);
}
