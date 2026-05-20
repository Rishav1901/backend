package com.infy.instagram.authentication.dto;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserDTO {
	
		private Long userId;
		@NotBlank(message="{auth.fullname.invalid}")
		@Pattern(regexp="([A-Z][a-z]*)+( [A-Za-z]+)*", message="{auth.fullname.invalid}")
		private String fullName;
		
		@NotBlank(message="{auth.email.invalid}")
		@Email(message="{auth.email.invalid}")
		private String email;
		
		@NotBlank(message="{auth.username.invalid}")
		private String username;
		@JsonIgnore
		@NotBlank(message="{auth.password.invalid}")
		@Pattern(regexp="^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&]).{8,16}",message="{auth.password.invalid}")
		private String password;
		
		private byte[] profilepic;
		
		private String bio;
		

	}



