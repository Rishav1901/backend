package com.insta.post.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "likes")
@Data
public class Like {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long likeId;

	private Long userId;

	@ManyToOne
	@JoinColumn(name = "post_id")
	private Post post;
	private LocalDateTime createdAt;
}
