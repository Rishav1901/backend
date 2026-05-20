package com.infy.instagram.authentication.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.infy.instagram.authentication.entity.User;

public interface UserRepository extends CrudRepository<User, Long> {
	
	Optional<User> findByusername(String username);
	
	Optional<User> findByEmail(String email);
	
	Boolean existsByUsername(String username);
	
	List<User> findByUsernameContainingIgnoreCase(String query);
	
	Boolean existsByEmail(String email);
	

}
