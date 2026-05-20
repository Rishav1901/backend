package com.insta.post.dto;

import lombok.Data;

@Data
public class PostMediaDTO {
	private Long mediaId;
	private MediaType mediaType;
	private Integer mediaOrder;
}
