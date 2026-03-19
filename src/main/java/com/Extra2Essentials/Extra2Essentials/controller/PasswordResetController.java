package com.Extra2Essentials.Extra2Essentials.controller;

import com.Extra2Essentials.Extra2Essentials.model.User;
import com.Extra2Essentials.Extra2Essentials.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.UUID;
import org.springframework.security.crypto.password.PasswordEncoder;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PasswordResetController {

    private final UserRepository userRepository;
    private final JavaMailSender mailSender;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        return userRepository.findByEmail(email).map(user -> {
            String token = UUID.randomUUID().toString();
            user.setResetToken(token);
            userRepository.save(user);

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Extra2Essential — Reset Your Password");
            message.setText("Click the link to reset your password:\n\n"
                + "http://localhost:8080/reset-password.html?token=" + token
                + "\n\nThis link expires in 1 hour.\n\nTeam Extra2Essential");
            mailSender.send(message);

            return ResponseEntity.ok("Reset link sent to your email!");
        }).orElse(ResponseEntity.badRequest().body("Email not found"));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> body) {
        String token = body.get("token");
        String newPassword = body.get("password");

        return userRepository.findAll().stream()
            .filter(u -> token.equals(u.getResetToken()))
            .findFirst()
            .map(user -> {
                user.setPassword(passwordEncoder.encode(newPassword));
                user.setResetToken(null);
                userRepository.save(user);
                return ResponseEntity.ok("Password reset successfully!");
            })
            .orElse(ResponseEntity.badRequest().body("Invalid or expired token"));
    }
}