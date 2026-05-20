package com.insta.post.service;

import java.util.List;

import com.insta.post.dto.ViewDTO;
import com.insta.post.exception.InstaPostManagementException;

public interface InstaPostViewService {
	Long viewPost(Long userId, Long postId) throws InstaPostManagementException;

	List<ViewDTO> getAllViewById(Long postId) throws InstaPostManagementException;

	Long countViewsByPostId(Long postId) throws InstaPostManagementException;
}
