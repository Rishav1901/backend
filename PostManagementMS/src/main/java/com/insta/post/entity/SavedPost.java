package com.insta.post.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
@Entity
@Table(name = "saved_posts")
@Data
public class SavedPost {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long savedPostId;
	private Long userId;

	@ManyToOne
	@JoinColumn(name = "post_id")
	private Post post;

	private LocalDateTime createdAt;
}
