package com.insta.post.dto;

import lombok.Data;

@Data
public class SavePostDTO {
	private Long savedPostId;
	private Long userId;
	private Long postId;
}
