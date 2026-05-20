package com.infy.instagram.follow.service;

import com.infy.instagram.follow.dto.SearchHistoryDTO;
import com.infy.instagram.follow.exception.FollowException;

import java.util.List;

public interface SearchHistoryService {

	void saveSearchHistory(Long userId, Long searchedUserId, String keyword) throws FollowException;

	List<SearchHistoryDTO> getSearchHistory(Long userId) throws FollowException;

	void clearSearchHistory(Long userId) throws FollowException;
}