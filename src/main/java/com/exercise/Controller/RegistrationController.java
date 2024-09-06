package com.exercise.Controller;

import com.exercise.Entity.MyUser;
import com.exercise.Entity.EmailVerification;
import com.exercise.Repository.MyUserRepository;
import com.exercise.Repository.EmailVerificationRepository;
import com.exercise.Service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.ResponseEntity;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.Set;

@Controller
public class RegistrationController {

    @Autowired
    private MyUserRepository myUserRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EmailVerificationRepository emailVerificationRepository;
    @Autowired
    private EmailService emailService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestParam String username,
                                          @RequestParam String email,
                                          @RequestParam String password,
                                          @RequestParam String confirmPassword) {
        // Basic validation
        if (!password.equals(confirmPassword)) {
            return ResponseEntity.badRequest().body("Passwords do not match");
        }

        // Check if username already  exists
        if (myUserRepository.findByUsername(username).isPresent()) {
            return ResponseEntity.badRequest().body("Username already exists");
        }
        if (myUserRepository.findByEmail(email).isPresent()) {
            return ResponseEntity.badRequest().body("Email already in use");
        }
        // Generate verification code
        String verificationCode = generateVerificationCode();

        // Save verification code to database
        EmailVerification emailVerification = new EmailVerification(email, verificationCode, LocalDateTime.now().plusHours(24));
        emailVerificationRepository.save(emailVerification);

        // Send verification email
        emailService.sendVerificationEmail(email, verificationCode);

        return ResponseEntity.ok("Verification code sent to your email");
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyEmail(@RequestParam String email,
                                         @RequestParam String code,
                                         @RequestParam String username,
                                         @RequestParam String password) {
        EmailVerification verification = emailVerificationRepository.findByEmail(email)
                .orElse(null);

        if (verification.getAttempts() >= 5) {
            emailVerificationRepository.delete(verification);
            return ResponseEntity.badRequest().body("Too many incorrect attempts. Please request a new verification code.");
        }
        if (!verification.getConfirmationCode().equals(code)) {
            verification.setAttempts(verification.getAttempts() + 1);
            emailVerificationRepository.save(verification);
            return ResponseEntity.badRequest().body("Invalid verification code. Attempts left: " + (5 - verification.getAttempts()));
        }

        if (verification.getExpiryDate().isBefore(LocalDateTime.now())) {
            emailVerificationRepository.delete(verification);
            emailService.sendVerificationEmail(email, generateVerificationCode());
            return ResponseEntity.badRequest().body("Verification code has expired");
        }

        // Create and save the new user
        MyUser newUser = new MyUser(username, email, passwordEncoder.encode(password));
        newUser.addRole("USER");
        newUser.setPermissions(Set.of("Read"));
        myUserRepository.save(newUser);

        // Delete the verification entry
        emailVerificationRepository.delete(verification);

        return ResponseEntity.ok("User registered successfully");
    }

    private String generateVerificationCode() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(1000000));
    }
}