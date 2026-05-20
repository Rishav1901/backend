package com.infy.instagram.authentication.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UpdateProfileRequestDTO {
	
	
	    @NotBlank(message="{auth.fullname.invalid}")
	    @Pattern(regexp="^[A-Z][a-z]*(?: [A-Z][a-z]*)*$", message="{auth.fullname.invalid}")
	    private String fullName;

	    @NotBlank(message="{auth.email.invalid}")
	    @Email(message="{auth.email.invalid}")
	    private String email;

	    private String bio;
	}




