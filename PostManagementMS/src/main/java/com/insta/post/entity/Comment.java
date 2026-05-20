package com.insta.post.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Data
public class Comment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long commentId;

	@ManyToOne
	@JoinColumn(name = "post_id")
	private Post post;

	private Long userId;

	private String comment;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

}