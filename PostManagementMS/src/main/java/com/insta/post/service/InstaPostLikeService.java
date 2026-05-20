package com.insta.post.service;

import java.util.List;

import com.insta.post.dto.LikeDTO;
import com.insta.post.exception.InstaPostManagementException;

public interface InstaPostLikeService {
	Long likePost(Long userId, Long postId2) throws InstaPostManagementException;

	Long unlikePost(Long userId, Long postId) throws InstaPostManagementException;

	List<LikeDTO> getAllLikeByPostId(Long postId) throws InstaPostManagementException;

	Long countLikesByPostId(Long postId) throws InstaPostManagementException;

	boolean isPostLikedByUser(Long userId, Long postId);
}
