package com.infy.instagram.authentication.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import lombok.Data;


@Data
public class UserRegistrationDTO {
	
	@NotBlank(message="{auth.fullname.invalid}")
	@Pattern(regexp="^[A-Z][a-z]*(?: [A-Z][a-z]*)*$", message="{auth.fullname.invalid}")
	private String fullName;
	
	@NotBlank(message="{auth.email.invalid}")
	@Email(message="{auth.email.invalid}")
	@Pattern(regexp="(?i)^[a-z0-9._%+-]+@[a-z0-9.-]+\\.(com|org|in)$", message="{auth.email.invalid}")
	private String email;
	
	@NotBlank(message="{auth.username.invalid}")
	@Pattern(regexp="^[a-z0-9._-]+$", message="{auth.username.invalid}")
	private String username;
	
	@NotBlank(message="{auth.password.invalid}")
	@Pattern(regexp="^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&]).{8,16}",message="{auth.password.invalid}")
	private String password;

	@NotBlank(message="{auth.password.invalid}")
	private String confirmPassword;
	
	

}
