package com.insta.post.dto;

import lombok.Data;

@Data
public class PostHashtagDTO {
	private Long postHashtagId;

	private Long postId;

	private HashtagDTO hashtag;
}
