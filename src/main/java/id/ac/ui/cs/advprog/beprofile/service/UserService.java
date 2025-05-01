package id.ac.ui.cs.advprog.beprofile.service;

import id.ac.ui.cs.advprog.beprofile.model.User;

public interface UserService {
    User getUserProfile(String id);
    User updateUserProfile(String id, User updatedUser);
    void deleteUserAccount(String id);
}