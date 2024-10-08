package com.exercise.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Setter
@Getter
@Entity
public class EmailVerification {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)

    private String email;


    private String confirmationCode;


    private LocalDateTime expiryDate;

    @Setter
    @Getter
    private int attempts;

    public EmailVerification() {
    }
    public EmailVerification(String email, String confirmationCode, LocalDateTime expiryDate) {
        this.email = email;
        this.confirmationCode = confirmationCode;
        this.expiryDate = expiryDate;
        attempts = 0;
    }

    public void setPassword(String password) {

    }
}