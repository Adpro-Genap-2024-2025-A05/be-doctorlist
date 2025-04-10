package id.ac.ui.cs.advprog.beprofile.service;

import id.ac.ui.cs.advprog.beprofile.model.Profile;
import id.ac.ui.cs.advprog.beprofile.repository.ProfileRepository;
import id.ac.ui.cs.advprog.beprofile.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProfileServiceTest {

    private ProfileService profileService;
    private ProfileRepository profileRepository;
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        profileRepository = mock(ProfileRepository.class);
        userRepository = mock(UserRepository.class);
        profileService = new ProfileService(userRepository, profileRepository);
    }

    @Test
    public void testGetProfile_ReturnsCorrectProfile() {
        // Arrange
        String userId = "user123";
        Profile dummyProfile = new Profile();
        dummyProfile.setUserId(userId);
        dummyProfile.setPhoneNumber("08123456789");

        when(profileRepository.findByUserId(userId)).thenReturn(Optional.of(dummyProfile));

        // Act
        Profile result = profileService.getProfile(userId);

        // Assert
        assertNotNull(result);
        assertEquals("08123456789", result.getPhoneNumber());
    }
}
