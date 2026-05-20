package com.insta.post.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;

@Entity
@Table(name = "shared_posts", uniqueConstraints = {
	@UniqueConstraint(columnNames = {"post_id", "user_id"})
})
@Data
public class SharedPost {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long sharedPostId;

	@ManyToOne
	@JoinColumn(name = "post_id")
	private Post post;

	@Column(name = "user_id", nullable = false)
	private Long userId;

	private String sharedMessage;

	private LocalDateTime sharedAt;
}
