package com.insta.post.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ViewDTO {
	private Long postViewId;

	private Long userId;

	private Long postId;

	private LocalDateTime viewedAt;
}
