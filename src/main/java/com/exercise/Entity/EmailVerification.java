package com.exercise.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;

@Entity
public class EmailVerification {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    @Setter
    @Getter
    private String email;

    @Setter
    @Getter
    private String confirmationCode;

    @Setter
    @Getter
    private LocalDateTime expiryDate;

    public EmailVerification() {
    }
    public EmailVerification(String email, String confirmationCode, LocalDateTime expiryDate) {
        this.email = email;
        this.confirmationCode = confirmationCode;
        this.expiryDate = expiryDate;
    }

    public void setPassword(String password) {

    }
}