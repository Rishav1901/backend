package com.insta.post.service;

import java.util.List;

import com.insta.post.dto.CommentDTO;
import com.insta.post.exception.InstaPostManagementException;

public interface InstaPostCommentService {
	Long commentPost(Long userId, Long postId, String comment) throws InstaPostManagementException;

	List<CommentDTO> getAllCommentPostId(Long postId) throws InstaPostManagementException;

	Long countCommentsByPostId(Long postId) throws InstaPostManagementException;

	void editComment(Long userId, Long postId, Long commentId, String newComment) throws InstaPostManagementException;

	void deleteComment(Long userId, Long postId, Long commentId) throws InstaPostManagementException;

}
