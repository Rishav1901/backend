package com.insta.post.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.insta.post.dto.PostDTO;
import com.insta.post.dto.PostResponseDTO;
import com.insta.post.dto.UserDTO;
import com.insta.post.exception.InstaPostManagementException;

public interface InstaPostService {
	
	PostDTO findPostById(Long postId) throws InstaPostManagementException;
	List<PostDTO> findPostsByUserId(Long userId) throws InstaPostManagementException;
	Long createPost(PostDTO dto,MultipartFile[] files) throws InstaPostManagementException;
	void deletePost(Long userId, Long postId) throws InstaPostManagementException;
	void editPost(Long userId, Long postId, PostDTO dto, MultipartFile[] files) throws InstaPostManagementException;
	void sharePost(Long userId, Long postId) throws InstaPostManagementException;
	void unsharePost(Long userId, Long postId) throws InstaPostManagementException;

	List<Object[]> findAllPostsWithoutData() throws InstaPostManagementException;
	List<PostDTO> getAllPosts() throws InstaPostManagementException;
	List<PostResponseDTO> getFeedPosts(Long userId) throws InstaPostManagementException;
	List<PostResponseDTO> getSavedPosts(Long userId) throws InstaPostManagementException;
	List<PostResponseDTO> getAllPostDetails(List<Long> postIds, Long loggedInUserId) throws InstaPostManagementException;

	PostResponseDTO getAllPostDetailsById(Long postId, Long currentUserId) throws InstaPostManagementException;
	UserDTO getUserDTO(Long userId);
	boolean isFollowingUser(Long currentUserId, Long targetUserId);
}
