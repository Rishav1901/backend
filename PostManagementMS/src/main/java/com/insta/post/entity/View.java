package com.insta.post.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "views")
@Data
public class View {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long postViewId;

	private Long userId;

	@ManyToOne
	@JoinColumn(name = "post_id")
	private Post post;

	private LocalDateTime viewedAt;
}
