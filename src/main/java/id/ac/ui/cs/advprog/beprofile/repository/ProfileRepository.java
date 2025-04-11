package id.ac.ui.cs.advprog.beprofile.repository;

import id.ac.ui.cs.advprog.beprofile.model.Profile;
import java.util.Optional;

public interface ProfileRepository {
    Optional<Profile> findByUserId(String userId);
    Profile save(Profile profile);
    void deleteByUserId(String userId);
}
