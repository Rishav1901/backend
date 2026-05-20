package com.infy.instagram.authentication.service;

public interface PasswordResetService {
    void generateResetTokenAndSendEmail(String email);
    void resetPassword(String token, String newPassword);
}