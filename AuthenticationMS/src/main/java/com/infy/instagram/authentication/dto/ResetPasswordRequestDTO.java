package com.infy.instagram.authentication.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ResetPasswordRequestDTO {
	
	@NotBlank(message="{auth.token.invalid}")
	private String token;
	
	@NotBlank(message="{auth.password.invalid}")
	@Pattern(regexp="^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&]).{8,16}",message="{auth.password.invalid}")
	private String newPassword;
	
	
	

}
