package com.infy.instagram.authentication.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.infy.instagram.authentication.entity.PasswordResetToken;

public interface PasswordResetRepository extends CrudRepository<PasswordResetToken, Long> {
	
	Optional<PasswordResetToken> findByToken(String token);

}
