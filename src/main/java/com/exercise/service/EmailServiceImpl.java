package com.exercise.service;

import com.exercise.entity.EmailVerification;
import com.exercise.repository.EmailVerificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private EmailVerificationRepository emailVerificationRepository;

    @Override
    public void sendVerificationEmail(String to, String verificationCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Email Verification");
        message.setText("Your verification code is: " + verificationCode);
        mailSender.send(message);
    }

    @Override
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