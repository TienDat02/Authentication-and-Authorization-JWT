package com.exercise.service;

public interface EmailService {
    void sendVerificationEmail(String to, String verificationCode);
    String verifyCode(String email, String code);
}