package com.insta.post.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class HashtagDTO {
	private Long hashtagId;

	private String tag;
	
	private Long usageCount;
	
	private LocalDateTime createdAt;
}
