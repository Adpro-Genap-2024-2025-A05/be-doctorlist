package id.ac.ui.cs.advprog.beprofile.controller;

import id.ac.ui.cs.advprog.beprofile.dto.ApiResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/")
@CrossOrigin(origins = "*")
public class HomeController {

    @GetMapping
    public ResponseEntity<ApiResponseDto<Map<String, String>>> healthCheck() {
        Map<String, String> status = new HashMap<>();
        status.put("status", "UP");
        status.put("service", "Back-End Doctor List API");
        
        return ResponseEntity.ok(
                ApiResponseDto.success(HttpStatus.OK.value(),
                                "Service is up and running",
                                status));
    }
}