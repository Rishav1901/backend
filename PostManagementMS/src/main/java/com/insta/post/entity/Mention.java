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
@Table(name = "mentions")
@Data
public class Mention {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long mentionId;

	@ManyToOne
	@JoinColumn(name = "post_id", nullable = true)
	private Post post;

	@ManyToOne
	@JoinColumn(name = "comment_id", nullable = true)
	private Comment comment;

	private Long mentionedUserId;

	private LocalDateTime createdAt;
}
