package com.infy.instagram.authentication.service;


import org.springframework.web.multipart.MultipartFile;

import com.infy.instagram.authentication.dto.JwtResponseDTO;
import com.infy.instagram.authentication.dto.LoginRequestDTO;
import com.infy.instagram.authentication.dto.UserRegistrationDTO;

import java.io.IOException;

public interface AuthService {
    void registerUser(UserRegistrationDTO registrationDto);
    JwtResponseDTO authenticateUser(LoginRequestDTO loginRequest);
    boolean checkIfUsernameExists(String username);
    void updateProfilePicture(Long userId, MultipartFile file) throws IOException;
    
}
