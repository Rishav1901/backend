package com.insta.post.service;

import java.util.List;

import com.insta.post.dto.SavePostDTO;
import com.insta.post.exception.InstaPostManagementException;

public interface InstaPostSavedService {
	Long createSavePost(Long userId, Long postId) throws InstaPostManagementException;

	List<SavePostDTO> getAllSavePost(Long postId) throws InstaPostManagementException;

	Long countSavesByPostId(Long postId) throws InstaPostManagementException;

	Long deleteSavePost(Long userId, Long postId) throws InstaPostManagementException;

	boolean isPostSavedByUser(Long userId, Long postId);

}
