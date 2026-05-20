package com.insta.post.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class PostResponseDTO {
	private Long postId;
	private UserDTO author;
	private String caption;
	private List<String> hashtags;
	private List<PostMediaDTO> mediaList = new ArrayList<>();
    private Long totalLikes;
    private Long totalComments;
    private Long totalViews;
    private Long totalSaves;
	private Boolean likedByUser;
	private Boolean savedByUser;
	private Visibility visibility;
	private LocalDateTime createdAt;
}
