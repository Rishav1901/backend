package com.infy.instagram.authentication.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenRefreshResponseDTO {

	private String accessToken;
	private String refreshToken;
}
