package com.infy.instagram.authentication.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponseDTO {
	
	private String accessToken;
	
	private String username;
	
	private Long userId;
	private String refreshToken;
	

}
