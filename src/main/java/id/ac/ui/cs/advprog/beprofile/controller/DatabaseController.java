package id.ac.ui.cs.advprog.beprofile.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/database")
public class DatabaseController {

    @PostMapping("/create")
    public ResponseEntity<String> createDatabase(@RequestParam String dbName) {
        try {
            // Jalankan perintah SQL untuk membuat database
            String sql = "CREATE DATABASE " + dbName;
            // Eksekusi SQL menggunakan JDBC atau Hibernate
            // (Implementasi tergantung pada kebutuhan Anda)
            return ResponseEntity.ok("Database " + dbName + " created successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error creating database: " + e.getMessage());
        }
    }
}