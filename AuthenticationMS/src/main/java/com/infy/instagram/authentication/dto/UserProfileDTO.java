package com.infy.instagram.authentication.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDTO {
    
	private Long userId;
	
	private String fullName;

	private String username;
	
	private String email;
	
	private byte[] profilepic;
	
	private String bio;
	
    private Long followersCount;
    
    private Long followingCount;
}
