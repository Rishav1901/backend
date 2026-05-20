package com.infy.instagram.authentication.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestDTO {

	@NotBlank(message="{auth.username.invalid}")
	private String username;
	
	@NotBlank(message="{auth.password.invalid}")
	private String password;
	
	
}
