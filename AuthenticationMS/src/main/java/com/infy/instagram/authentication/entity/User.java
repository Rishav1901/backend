package com.infy.instagram.authentication.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long userId;
	@Column(nullable = false, length=100)
	private String fullName;
	@Column(name="username",unique=true, nullable =false ,length=50)
	private String username;
	
	@Column(unique=true, nullable =false ,length=100)
	private String email;
	
	@Column(nullable=false)
	private String password;
	
	@Lob
	@Column(name="profile_picture",columnDefinition = "MEDIUMBLOB")
	private byte[] profilepic;
	
	private String bio;
	
	@CreationTimestamp
	private LocalDateTime createdAt;
	
	@UpdateTimestamp
	private LocalDateTime updatedAt;

}
