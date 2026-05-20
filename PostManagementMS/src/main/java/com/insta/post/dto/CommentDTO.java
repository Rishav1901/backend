package com.insta.post.dto;

import java.time.LocalDateTime;

import lombok.Data;
@Data
public class CommentDTO {
	private Long commentId;

	private Long postId;

	private Long userId;

	private String username;

	private String comment;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;
}
