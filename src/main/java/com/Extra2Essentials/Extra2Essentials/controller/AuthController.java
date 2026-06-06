package com.Extra2Essentials.Extra2Essentials.controller;

import com.Extra2Essentials.Extra2Essentials.model.*;
import com.Extra2Essentials.Extra2Essentials.repository.UserRepository;
import com.Extra2Essentials.Extra2Essentials.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> body) {
        if (userRepository.existsByEmail(body.get("email"))) {
            return ResponseEntity.badRequest().body("Email already exists");
        }
        User user = User.builder()
            .name(body.get("name"))
            .email(body.get("email"))
            .password(passwordEncoder.encode(body.get("password")))
            .role(Role.valueOf(body.get("role").toUpperCase()))
            .city(body.get("city"))
            .phone(body.get("phone"))
            .companyName(body.get("companyName"))
            .industry(body.get("industry"))
            .gstin(body.get("gstin"))
            .annualRevenue(body.get("annualRevenue") != null ? 
                Double.parseDouble(body.get("annualRevenue")) : null)
            .csrBudget(body.get("csrBudget") != null ? 
                Double.parseDouble(body.get("csrBudget")) : null)
            .build();
        userRepository.save(user);
        return ResponseEntity.ok("Registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        return userRepository.findByEmail(body.get("email"))
                .filter(u -> passwordEncoder.matches(body.get("password"), u.getPassword()))
                .map(u -> ResponseEntity.ok(Map.of(
                        "token", jwtUtil.generateToken(u.getEmail(), u.getRole().name()),
                        "role", u.getRole().name(),
                        "name", u.getName()
                )))
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Invalid credentials")));
    }

    // Get my profile (includes saved address)
    @GetMapping("/profile")
        public ResponseEntity<?> getProfile(Authentication auth) {
            return userRepository.findByEmail(auth.getName()).map(user -> {
                java.util.Map<String, Object> response = new java.util.HashMap<>();
                response.put("id", user.getId());
                response.put("name", user.getName());
                response.put("email", user.getEmail());
                response.put("role", user.getRole());
                response.put("city", user.getCity() != null ? user.getCity() : "");
                response.put("phone", user.getPhone() != null ? user.getPhone() : "");
                response.put("address", user.getAddress() != null ? user.getAddress() : "");
                response.put("latitude", user.getLatitude() != null ? user.getLatitude() : "");
                response.put("longitude", user.getLongitude() != null ? user.getLongitude() : "");
                response.put("companyName", user.getCompanyName() != null ? user.getCompanyName() : "");
                response.put("industry", user.getIndustry() != null ? user.getIndustry() : "");
                response.put("gstin", user.getGstin() != null ? user.getGstin() : "");
                response.put("annualRevenue", user.getAnnualRevenue() != null ? user.getAnnualRevenue() : 0);
                response.put("csrBudget", user.getCsrBudget() != null ? user.getCsrBudget() : 0);
                response.put("csrSpent", user.getCsrSpent() != null ? user.getCsrSpent() : 0);
                return ResponseEntity.ok(response);
            }).orElse(ResponseEntity.notFound().build());
        }

    // Update profile address
    @PutMapping("/profile/address")
    public ResponseEntity<?> updateAddress(@RequestBody Map<String, String> body,
                                            Authentication auth) {
        return userRepository.findByEmail(auth.getName()).map(u -> {
            if (body.get("address") != null) u.setAddress(body.get("address"));
            if (body.get("city") != null) u.setCity(body.get("city"));
            if (body.get("latitude") != null && !body.get("latitude").isEmpty())
                u.setLatitude(Double.parseDouble(body.get("latitude")));
            if (body.get("longitude") != null && !body.get("longitude").isEmpty())
                u.setLongitude(Double.parseDouble(body.get("longitude")));
            userRepository.save(u);
            return ResponseEntity.ok("Address saved successfully");
        }).orElse(ResponseEntity.notFound().build());
    }
}