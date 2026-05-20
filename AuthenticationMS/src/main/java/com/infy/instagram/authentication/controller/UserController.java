package com.infy.instagram.authentication.controller;



import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.infy.instagram.authentication.dto.FollowCountDTO;

import com.infy.instagram.authentication.dto.UpdateProfileRequestDTO;
import com.infy.instagram.authentication.dto.UserDTO;
import com.infy.instagram.authentication.dto.UserProfileDTO;
import com.infy.instagram.authentication.exception.UserException;
import com.infy.instagram.authentication.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value="/api/users")
@RequiredArgsConstructor
public class UserController {
	
	private final UserService userService;
	private final  WebClient.Builder webBuilder;
	private final ModelMapper mapper;
	
	@GetMapping(value="/{username}")
	public ResponseEntity<UserProfileDTO>  getProfile(@PathVariable String username) throws UserException {
		System.out.println("called");
		UserDTO userDTO =userService.getUserProfile(username);
		FollowCountDTO countDTO = webBuilder.build().get().uri("http://FollowMS/api/follows/count/"+ userDTO.getUserId()).retrieve().bodyToMono(FollowCountDTO.class).block();
		UserProfileDTO dto = mapper.map(userDTO, UserProfileDTO.class);
		dto.setFollowersCount(countDTO.getFollowersCount());
		dto.setFollowingCount(countDTO.getFollowingCount());
		return new ResponseEntity<UserProfileDTO>(dto,HttpStatus.OK);	
	}
	@GetMapping(value="/userId/{userId}")
	public ResponseEntity<UserProfileDTO>  getProfileById(@PathVariable Long userId) throws UserException {
		System.out.println("called");
		UserProfileDTO userDTO =userService.getUserProfileById(userId);
		FollowCountDTO countDTO = webBuilder.build().get().uri("http://FollowMS/api/follows/count/"+ userId).retrieve().bodyToMono(FollowCountDTO.class).block();
		userDTO.setFollowersCount(countDTO.getFollowersCount());
		userDTO.setFollowingCount(countDTO.getFollowingCount());
		return new ResponseEntity<UserProfileDTO>(userDTO,HttpStatus.OK); 	
	}
//	@GetMapping(value="/search")
//	public ResponseEntity<List<UserDTO>> getUsersBySearch(@RequestParam("query") String query ){
//		
//		List<UserDTO> userDTOs =userService.searchUsers(query);
//		
//		return new ResponseEntity<>(userDTOs,HttpStatus.OK);
//		
//	}
	@GetMapping(value="/search")
	public ResponseEntity<List<UserDTO>> getUsersBySearch(@RequestParam("query") String query ){
		
		List<UserDTO> userDTOs =userService.searchUsers(query);
		
		return new ResponseEntity<>(userDTOs,HttpStatus.OK);
		
	}
	
//	@PutMapping(value="/profile")
//	public ResponseEntity<String> updateProfile(@Valid @RequestBody UserDTO userDTO ) throws UserException{
//		
//		
//		Long id =userService.updateUserProfile(userDTO);
//		String success = "User "+id+ "updated successfully";
//		
//		return new ResponseEntity<String>(success,HttpStatus.OK);}}


@PutMapping(value="/profile")
public ResponseEntity<String> updateProfile(
        @RequestHeader
        ("X-User-Id") Long userId, 
        @Valid @RequestBody UpdateProfileRequestDTO updateDTO) throws UserException {
    
    userService.updateUserProfile(userId, updateDTO);
    String success = "User " + userId + " updated successfully";
    
    return new ResponseEntity<>(success, HttpStatus.OK);
}
}