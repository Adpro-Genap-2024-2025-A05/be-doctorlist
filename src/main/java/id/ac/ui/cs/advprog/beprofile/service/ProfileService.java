package id.ac.ui.cs.advprog.beprofile.service;

import id.ac.ui.cs.advprog.beprofile.model.Profile;
import id.ac.ui.cs.advprog.beprofile.repository.ProfileRepository;
import id.ac.ui.cs.advprog.beprofile.repository.UserRepository;

public class ProfileService {
    private final UserRepository userRepo;
    private final ProfileRepository profileRepo;

    public ProfileService(UserRepository userRepo, ProfileRepository profileRepo) {
        this.userRepo = userRepo;
        this.profileRepo = profileRepo;
    }

    public Profile getProfile(String userId) {
        return profileRepo.findByUserId(userId).orElseThrow();
    }

    public Profile updateProfile(String userId, Profile updated) {
        Profile existing = profileRepo.findByUserId(userId).orElseThrow();
        existing.setPhoneNumber(updated.getPhoneNumber());
        existing.setAddress(updated.getAddress());
        existing.setDateOfBirth(updated.getDateOfBirth());
        return profileRepo.save(existing);
    }

    public void deleteAccount(String userId) {
        userRepo.deleteById(userId);
        profileRepo.deleteByUserId(userId);
    }
}
