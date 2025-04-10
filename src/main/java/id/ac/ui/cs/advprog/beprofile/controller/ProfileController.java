package id.ac.ui.cs.advprog.beprofile.controller;

import id.ac.ui.cs.advprog.beprofile.model.Profile;
import id.ac.ui.cs.advprog.beprofile.security.SecurityService;
import id.ac.ui.cs.advprog.beprofile.service.ProfileService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/profile")
public class ProfileController {

    private final ProfileService profileService;
    private final SecurityService securityService;

    public ProfileController(ProfileService profileService, SecurityService securityService) {
        this.profileService = profileService;
        this.securityService = securityService;
    }

    @GetMapping
    public ResponseEntity<Profile> getProfile(@RequestHeader("Authorization") String token,
                                              @RequestParam String userId) {
        if (!isAuthorized(token, userId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Profile profile = profileService.getProfile(userId);
        return ResponseEntity.ok(profile);
    }

    @PutMapping
    public ResponseEntity<Profile> updateProfile(@RequestHeader("Authorization") String token,
                                                 @RequestParam String userId,
                                                 @RequestBody Profile profile) {
        if (!isAuthorized(token, userId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(profileService.updateProfile(userId, profile));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAccount(@RequestHeader("Authorization") String token,
                                              @RequestParam String userId) {
        if (!isAuthorized(token, userId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        profileService.deleteAccount(userId);
        return ResponseEntity.noContent().build();
    }

    private boolean isAuthorized(String token, String userId) {
        return securityService.authenticate(token) && securityService.authorize(userId, extractUserId(token));
    }

    private String extractUserId(String token) {
        return "mockUserId"; // Dummy, nanti ganti dengan decode dari JWT
    }
}
