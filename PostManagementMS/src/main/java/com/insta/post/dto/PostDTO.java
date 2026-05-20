package com.insta.post.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PostDTO {

	private Long postId;
	private Long userId;
	@NotBlank(message = "Please provide a valid caption")
	private String caption;
	@NotNull(message = "Please provide a valid visibility")
	private Visibility visibility;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private List<PostMediaDTO> mediaList = new ArrayList<>();
}
