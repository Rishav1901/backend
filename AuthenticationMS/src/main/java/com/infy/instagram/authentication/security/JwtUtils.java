package com.infy.instagram.authentication.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;



import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtils {
	
	@Value("${app.jwtSecret}")
	private String jwtSecret;
	private static final Log LOGGER = LogFactory.getLog(JwtUtils.class);
	private SecretKey key() {
		return Keys.hmacShaKeyFor(jwtSecret.getBytes());
	}
	
	public String generateJwtToken(String username, Long userId) {
		return Jwts.builder()
				.subject(username)
				.claim("userId", userId)
				.issuedAt(new Date())
				.expiration(new Date(System.currentTimeMillis()+1000*60*20))
				.signWith(key())
				.compact();
	}
	
	
	public boolean validateJwtToken(String authToken) {
		try {
		Jwts.parser().verifyWith(key()).build().parseSignedClaims(authToken);
		return true;
		}
		catch(JwtException | IllegalArgumentException exception) {
			LOGGER.error(exception.getMessage(), exception);
		}
		return false;
	}
	
	public String getUsernameFromJwt(String token) {
		return Jwts.parser().verifyWith(key()).build().parseSignedClaims(token).getPayload().getSubject();
		
		
	}
	
	
	

}
