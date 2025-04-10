package id.ac.ui.cs.advprog.beprofile.controller;

import id.ac.ui.cs.advprog.beprofile.model.Profile;
import id.ac.ui.cs.advprog.beprofile.security.SecurityService;
import id.ac.ui.cs.advprog.beprofile.service.ProfileService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProfileController.class)
public class ProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProfileService profileService;

    @MockBean
    private SecurityService securityService;

    @Test
    void testGetProfile_shouldReturnProfile_whenAuthorized() throws Exception {
        // Create mock Profile object
        Profile profile = createMockProfile();

        // Mocking the behavior of security and profileService
        when(securityService.authenticate("Bearer mocktoken")).thenReturn(true);
        when(securityService.authorize("user123", "mockUserId")).thenReturn(true);
        when(profileService.getProfile("user123")).thenReturn(profile);

        // Perform the GET request and verify the response
        mockMvc.perform(get("/user/profile")
                        .header("Authorization", "Bearer mocktoken")
                        .param("userId", "user123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.phoneNumber").value("081234567890"));
    }

    // Helper method to create a mock Profile object
    private Profile createMockProfile() {
        Profile profile = new Profile();
        profile.setUserId("user123");
        profile.setPhoneNumber("081234567890");
        return profile;
    }
}
