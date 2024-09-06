package com.exercise.Controller;

import com.exercise.Entity.EmailVerification;
import com.exercise.Entity.MyUser;
import com.exercise.Repository.EmailVerificationRepository;
import com.exercise.Repository.MyUserRepository;
import com.exercise.Service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

@RestController
public class ForgotPasswordController {

    @Autowired
    private MyUserRepository myUserRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private EmailVerificationRepository emailVerificationRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        // Kiểm tra email tồn tại trong hệ thống

        if (myUserRepository.findByEmail(email).isEmpty()) {
            return ResponseEntity.badRequest().body("Email not exists");
        }

        Optional<EmailVerification> existingVerification = emailVerificationRepository.findByEmail(email);
        existingVerification.ifPresent(emailVerification -> emailVerificationRepository.delete(emailVerification));
        // Gửi mã xác minh qua email
        String verificationCode = generateVerificationCode();

        // Save verification code to database
        EmailVerification emailVerification = new EmailVerification(email, verificationCode, LocalDateTime.now().plusHours(24));
        emailVerificationRepository.save(emailVerification);

        // Send verification email
        emailService.sendVerificationEmail(email,verificationCode);


        return ResponseEntity.ok("Verification code sent to your email");
    }


    @PutMapping("/reset-password")
    @ResponseBody
    public ResponseEntity<?> resetPassword(@RequestParam String email,
                                           @RequestParam String code,
                                           @RequestParam String newPassword,
                                           @RequestParam String confirmPassword) {
        if (!newPassword.equals(confirmPassword)) {
            return ResponseEntity.badRequest().body("New password and confirm password do not match.");
        }

        // Kiểm tra mã xác minh
        EmailVerification verification = emailVerificationRepository.findByEmail(email)
                .orElse(null);
        if (verification == null || !Objects.equals(verification.getConfirmationCode(), code)) {
            return ResponseEntity.badRequest().body("Invalid verification code");
        }


        if (verification.getExpiryDate().isBefore(LocalDateTime.now())) {
            return ResponseEntity.badRequest().body("Verification code has expired");
        }

        // Kiểm tra email người dùng tồn tại
        Optional<MyUser> userOptional = myUserRepository.findByEmail(email);
        if (!userOptional.isPresent()) {
            return ResponseEntity.badRequest().body("Email does not exist.");
        }

        // Cập nhật mật khẩu mới
        MyUser user = userOptional.get();
        user.setPassword(passwordEncoder.encode(newPassword));
        myUserRepository.save(user);

        return ResponseEntity.ok("Password has been successfully updated.");
    }

    private String generateVerificationCode() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(1000000));
    }
}