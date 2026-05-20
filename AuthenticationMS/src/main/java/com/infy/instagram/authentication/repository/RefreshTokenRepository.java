package com.infy.instagram.authentication.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


import com.infy.instagram.authentication.entity.RefreshToken;
import com.infy.instagram.authentication.entity.User;

public interface RefreshTokenRepository  extends JpaRepository<RefreshToken, Long>{
	Optional<RefreshToken> findByToken(String token);
	
	void deleteByUser(User user);

}
