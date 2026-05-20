package com.infy.instagram.authentication.service;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.infy.instagram.authentication.dto.JwtResponseDTO;
import com.infy.instagram.authentication.dto.LoginRequestDTO;
import com.infy.instagram.authentication.dto.UserRegistrationDTO;
import com.infy.instagram.authentication.entity.User;
import com.infy.instagram.authentication.repository.RefreshTokenRepository;
import com.infy.instagram.authentication.repository.UserRepository;
import com.infy.instagram.authentication.security.JwtUtils;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.transaction.Transactional;



@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl  implements AuthService{

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final  RefreshTokenRepository refreshTokenRepository;
    private final JwtUtils jwtUtils;

    public void registerUser(UserRegistrationDTO registrationDTO) {
        if (!registrationDTO.getPassword().equals(registrationDTO.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }
        if (userRepository.existsByUsername(registrationDTO.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (userRepository.existsByEmail(registrationDTO.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }

        User user = User.builder()
                .fullName(registrationDTO.getFullName())
                .username(registrationDTO.getUsername())
                .email(registrationDTO.getEmail())
                .password(passwordEncoder.encode(registrationDTO.getPassword()))
                .build();

        userRepository.save(user);
    }

    @CircuitBreaker(name = "loginCircuit", fallbackMethod = "loginFallback")
    public JwtResponseDTO authenticateUser(LoginRequestDTO loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        User user = userRepository.findByusername(loginRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found after authentication"));
        refreshTokenRepository.deleteByUser(user);
        refreshTokenRepository.flush();
       

        String jwt = jwtUtils.generateJwtToken(user.getUsername(), user.getUserId());

        return new JwtResponseDTO(jwt, user.getUsername(), user.getUserId(), null);
    }

    public JwtResponseDTO loginFallback(LoginRequestDTO loginRequest, Throwable ex) {
        if (ex instanceof io.github.resilience4j.circuitbreaker.CallNotPermittedException) {
            throw new RuntimeException("Service is currently unavailable (multiple attempts). Please try again after 1 minute.");
        }
        if (ex instanceof org.springframework.security.core.AuthenticationException) {
            throw new IllegalArgumentException("Invalid username or password");
        }
        throw new RuntimeException(ex.getMessage());
    }

    public boolean checkIfUsernameExists(String username) {
        return userRepository.existsByUsername(username);
    }

    public void updateProfilePicture(Long userId, MultipartFile file) throws IOException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("Only image files are allowed");
        }

        user.setProfilepic(file.getBytes());
        userRepository.save(user);
    }
}

