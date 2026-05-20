package com.insta.post.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class LikeDTO {
	private Long likeId;

	private Long userId;

	private Long postId;

	private LocalDateTime createdAt;
}
