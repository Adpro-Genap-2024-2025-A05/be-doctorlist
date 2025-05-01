package id.ac.ui.cs.advprog.beprofile.controller;

import id.ac.ui.cs.advprog.beprofile.model.ConsultationHistory;
import id.ac.ui.cs.advprog.beprofile.model.User;
import id.ac.ui.cs.advprog.beprofile.service.ConsultationHistoryService;
import id.ac.ui.cs.advprog.beprofile.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ConsultationHistoryService consultationHistoryService;

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserProfile(@PathVariable String id) {
        User user = userService.getUserProfile(id);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUserProfile(@PathVariable String id, @RequestBody User updatedUser) {
        User user = userService.updateUserProfile(id, updatedUser);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserAccount(@PathVariable String id) {
        userService.deleteUserAccount(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/consultations")
    public ResponseEntity<List<ConsultationHistory>> getConsultationHistory(@PathVariable String id) {
        List<ConsultationHistory> history = consultationHistoryService.getConsultationHistory(id);
        return ResponseEntity.ok(history);
    }
}