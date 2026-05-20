package com.infy.instagram.authentication.security;
import java.util.List;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final CustomUserDetailsService userDetailsService;

	// Password encoder (using a secure algorithm like BCryptPasswordEncoder)
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider(){
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		
		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());
		
		return authProvider;
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		
		return authConfig.getAuthenticationManager();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http.csrf(csrf -> csrf.disable())
		.sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		
		.authorizeHttpRequests(auth -> auth.requestMatchers("/api/auth/**")
				
				.permitAll()
				.requestMatchers("/api/users/**")
				.permitAll()
				.requestMatchers("/actuator/**").permitAll()
				
				.requestMatchers("/v3/api-docs/**","/swagger-ui/**","/swagger-ui/html").permitAll()
				.anyRequest().authenticated()
				
				);
	http.authenticationProvider(authenticationProvider());
	
		
	



		//http.cors(cors -> cors.configurationSource(configureCors()));

		return http.build();

	}

	// Security configuration with CORS integration

	@Bean
	public CorsConfigurationSource configureCors() {
		CorsConfiguration config = new CorsConfiguration().applyPermitDefaultValues();
		config.setAllowedOrigins(List.of("http://localhost:4200", "http://localhost:3000"));
		return request -> config;
	}
}



