package com.exercise.repository;

import com.exercise.entity.EmailVerification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailVerificationRepository extends JpaRepository<EmailVerification, Long> {
    Optional<EmailVerification> findByEmail(String email);
    Optional<EmailVerification> findByConfirmationCode(String confirmationCode);
    void deleteByEmail(String email);
}