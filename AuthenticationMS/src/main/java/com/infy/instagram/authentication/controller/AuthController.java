package com.infy.instagram.authentication.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import com.infy.instagram.authentication.dto.JwtResponseDTO;
import com.infy.instagram.authentication.dto.LoginRequestDTO;
import com.infy.instagram.authentication.dto.ResetPasswordRequestDTO;
import com.infy.instagram.authentication.dto.TokenRefreshRequestDTO;
import com.infy.instagram.authentication.dto.TokenRefreshResponseDTO;
import com.infy.instagram.authentication.dto.UserRegistrationDTO;

import com.infy.instagram.authentication.entity.User;
import com.infy.instagram.authentication.repository.UserRepository;
import com.infy.instagram.authentication.security.JwtUtils;
import com.infy.instagram.authentication.service.AuthService;
import com.infy.instagram.authentication.service.AuthServiceImpl;
import com.infy.instagram.authentication.service.PasswordResetService;
import com.infy.instagram.authentication.service.PasswordResetServiceImpl;
import com.infy.instagram.authentication.service.RefreshTokenService;
import com.infy.instagram.authentication.service.RefreshTokenServiceImpl;

@RestController
@RequestMapping("/api/auth")
@Validated
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;
    private final PasswordResetService passwordResetService;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;

    @PostMapping(value="/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody UserRegistrationDTO request) {
        authService.registerUser(request);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping(value="/login")
    public ResponseEntity<JwtResponseDTO> authenticateUser(@Valid @RequestBody LoginRequestDTO request) {
        JwtResponseDTO response = authService.authenticateUser(request);
        response.setRefreshToken(refreshTokenService.createRefreshToken(response.getUserId()).getToken());
        return ResponseEntity.ok(response);
    }

    @GetMapping(value="/check-username")
    public ResponseEntity<Boolean> checkUsernameAvailability(
            @RequestParam @NotBlank(message = "{auth.username.invalid}") String username) {
        return ResponseEntity.ok(!authService.checkIfUsernameExists(username));
    }

    @PostMapping(value="/refresh-token")
    public ResponseEntity<TokenRefreshResponseDTO> refreshToken(@Valid @RequestBody TokenRefreshRequestDTO request) {
        return refreshTokenService.findByToken(request.getRefreshToken())
                .map(token->refreshTokenService.verifyExpiration(token))
                .map(validToken->validToken.getUser())
                .map(user -> {
                    String jwt = jwtUtils.generateJwtToken(user.getUsername(), user.getUserId());
                    return ResponseEntity.ok(new TokenRefreshResponseDTO(jwt, request.getRefreshToken()));
                })
                .orElseThrow(() -> new RuntimeException("Refresh token is not in database or is invalid"));
    }

    @PostMapping(value="/forgot-password")
    public ResponseEntity<String> forgotPassword(
            @RequestParam @NotBlank(message = "{auth.email.invalid}") String email) {
        passwordResetService.generateResetTokenAndSendEmail(email);
        return ResponseEntity.ok("Password reset link sent to your email");
    }

    @PostMapping(value="/reset-password")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody ResetPasswordRequestDTO request) {
        passwordResetService.resetPassword(request.getToken(), request.getNewPassword());
        return ResponseEntity.ok("Password has been successfully reset");
    }

    @PostMapping(value = "/profile-picture", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadProfilePicture(
            @RequestHeader("X-User-Id") @NotNull @Positive Long userId,
            @RequestParam("file") MultipartFile file) {
        try {
        	System.out.println("called");
            authService.updateProfilePicture(userId, file);
            return ResponseEntity.ok("Profile picture uploaded successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Could not upload file: " + e.getMessage());
        }
    }

    @GetMapping(value="/profile-picture/{userId}")
    public ResponseEntity<byte[]> getProfilePicture(@PathVariable Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        byte[] imageBytes = user.getProfilepic();
        if (imageBytes == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .header(HttpHeaders.CACHE_CONTROL, "max-age=3600")
                .body(imageBytes);
    }
    
 
    
    
    
}