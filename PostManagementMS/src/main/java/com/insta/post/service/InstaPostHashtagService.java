package com.insta.post.service;

import java.util.List;

import com.insta.post.dto.HashtagDTO;
import com.insta.post.entity.Hashtag;
import com.insta.post.exception.InstaPostManagementException;

public interface InstaPostHashtagService {
	Hashtag createHashtag(String tag) throws InstaPostManagementException;

	HashtagDTO findHashtagById(Long hashtagId) throws InstaPostManagementException;

	HashtagDTO findHashtagByTag(String tag) throws InstaPostManagementException;

	Long postHashtag(Long postId, String tag) throws InstaPostManagementException;

	List<String>  getPostHashtag(Long postId) throws InstaPostManagementException;
	
	List<HashtagDTO> findAllHashtags() throws InstaPostManagementException;
	
	List<HashtagDTO> findByQuerytag(String tag) throws InstaPostManagementException;
	
	List<Long> findDistinctPostIdsByHashtagId(Long hashtagId) throws InstaPostManagementException;
	
	List<HashtagDTO> findByTagIdList(List<Long> list) throws InstaPostManagementException;

	List<String> extractHashtags(String text) throws InstaPostManagementException;

	void deletePostHashtags(Long postId) throws InstaPostManagementException;

	List<Long> extractMentionedUserIds(String text) throws InstaPostManagementException;

}
