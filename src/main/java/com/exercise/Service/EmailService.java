package com.exercise.Service;

import com.exercise.Entity.EmailVerification;
import com.exercise.Entity.MyUser;
import com.exercise.Repository.EmailVerificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private EmailVerificationRepository emailVerificationRepository;
    public void sendVerificationEmail(String to, String verificationCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Email Verification");
        message.setText("Your verification code is: " + verificationCode);
        mailSender.send(message);
    }


    public String verifyCode(String email, String code) {
        Optional<EmailVerification> verificationOptional = emailVerificationRepository.findByConfirmationCode(code);

        if (!verificationOptional.isPresent()) {
            return "Mã xác minh không hợp lệ.";
        }

        EmailVerification verification = verificationOptional.get();
        if (!verification.getEmail().equals(email)) {
            return "Email không khớp.";
        }

        if (verification.getExpiryDate().isBefore(LocalDateTime.now())) {
            return "Mã xác minh đã hết hạn.";
        }

        // Xóa mã xác minh sau khi xác thực thành công
        emailVerificationRepository.delete(verification);
        return "VALID";
    }




}