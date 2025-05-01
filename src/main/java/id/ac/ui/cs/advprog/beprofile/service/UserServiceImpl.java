package id.ac.ui.cs.advprog.beprofile.service.impl;

import id.ac.ui.cs.advprog.beprofile.model.User;
import id.ac.ui.cs.advprog.beprofile.repository.UserRepository;
import id.ac.ui.cs.advprog.beprofile.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User getUserProfile(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));
    }

    @Override
    public User updateUserProfile(String id, User updatedUser) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));
        existingUser.setFullname(updatedUser.getFullname());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setPhoneNumber(updatedUser.getPhoneNumber());
        return userRepository.save(existingUser);
    }

    @Override
    public void deleteUserAccount(String id) {
        userRepository.deleteById(id);
    }
}