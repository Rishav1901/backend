package com.infy.instagram.authentication.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TokenRefreshRequestDTO {

	
	@NotBlank(message="{auth.refreshtoken.invalid}")
	private String refreshToken;
	
}
