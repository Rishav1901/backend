package com.infy.instagram.authentication.service;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.infy.instagram.authentication.dto.UpdateProfileRequestDTO;
import com.infy.instagram.authentication.dto.UserDTO;
import com.infy.instagram.authentication.dto.UserProfileDTO;
import com.infy.instagram.authentication.entity.User;
import com.infy.instagram.authentication.exception.UserException;
import com.infy.instagram.authentication.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	private final ModelMapper modelMapper;
	 
	public UserDTO getUserProfile(String username) throws UserException {
		
		User user =userRepository.findByusername(username).orElseThrow(()->new UserException("User with username"+username+"is not found"));
		UserDTO profile =modelMapper.map(user, UserDTO.class);
		return profile;
		
		
	}
	public List<UserDTO> searchUsers(String query){
		
		
		List<User> users = userRepository.findByUsernameContainingIgnoreCase(query);
		List<UserDTO> userDTOs = new ArrayList<>();
		
		for(User user :users) {
			UserDTO newDTO = modelMapper.map(user,UserDTO.class);
			userDTOs.add(newDTO);
			
		}
		
		return userDTOs;
		
		

		
	}
	
//	public Long updateUserProfile(UserDTO userDTO) throws UserException{
//		User user = userRepository.findByusername(userDTO.getUsername()).orElseThrow(()-> new UserException("Username"+userDTO.getUsername()+"Not found"));
//		System.out.println(user);
//		if(!user.getFullName().equals(userDTO.getFullName())) {
//			user.setFullName(userDTO.getFullName());
//		}
//		if(!user.getEmail().equals(userDTO.getEmail())) {
//			user.setEmail(userDTO.getEmail());
//		}
//		if(user.getBio()==null || !user.getBio().equals(userDTO.getBio())) {
//			user.setBio(userDTO.getBio());
//		}
//		
//
//		
//		return user.getUserId();

	//}
	public Long updateUserProfile(Long userId, UpdateProfileRequestDTO updateDTO) throws UserException {
	    
	    // Find the user by ID instead of username
	    User user = userRepository.findById(userId)
	            .orElseThrow(() -> new UserException("User not found"));

	    if (updateDTO.getFullName() != null && !updateDTO.getFullName().equals(user.getFullName())) {
	        user.setFullName(updateDTO.getFullName());
	    }
	    
	    if (updateDTO.getEmail() != null && !updateDTO.getEmail().equals(user.getEmail())) {
	        user.setEmail(updateDTO.getEmail());
	    }
	    
	    if (updateDTO.getBio() != null && !updateDTO.getBio().equals(user.getBio())) {
	        user.setBio(updateDTO.getBio());
	    }
	    
	    // userRepository.save(user); // Don't forget to save if not  @Transactional
	    
	    return user.getUserId();
	}
	public UserProfileDTO getUserProfileById(Long userId) throws UserException {
		User user =userRepository.findById(userId).orElseThrow(()->new UserException("User with id"+userId+"is not found"));
		UserProfileDTO profile =modelMapper.map(user, UserProfileDTO.class);
		return profile;
		
	}
	
	
	
	
	
	
	

}
