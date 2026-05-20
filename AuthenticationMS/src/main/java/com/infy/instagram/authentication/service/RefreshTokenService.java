package com.infy.instagram.authentication.service;

import java.util.Optional;

import com.infy.instagram.authentication.entity.RefreshToken;

public interface RefreshTokenService {
    RefreshToken createRefreshToken(Long userId);
    Optional<RefreshToken> findByToken(String token);
    RefreshToken verifyExpiration(RefreshToken token);
}