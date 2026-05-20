package com.infy.instagram.follow.service;

import com.infy.instagram.follow.dto.SearchHistoryDTO;
import com.infy.instagram.follow.entity.SearchHistory;
import com.infy.instagram.follow.exception.FollowException;
import com.infy.instagram.follow.repository.SearchHistoryRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SearchHistoryServiceImpl implements SearchHistoryService {

	private final SearchHistoryRepository searchHistoryRepository;

	private final ModelMapper modelMapper;

	@Override
	public void saveSearchHistory(Long userId, Long searchedUserId, String keyword) throws FollowException {

		if (userId == null) {

			throw new FollowException("User Id cannot be null", HttpStatus.BAD_REQUEST);
		}

		if (searchedUserId == null && (keyword == null || keyword.isBlank())) {

			throw new FollowException("Search keyword or searched user id is required", HttpStatus.BAD_REQUEST);
		}

		SearchHistory history = SearchHistory.builder().userId(userId).searchedUserId(searchedUserId)
				.searchedKeyword(keyword).build();

		searchHistoryRepository.save(history);
	}

	@Override
	public List<SearchHistoryDTO> getSearchHistory(Long userId) throws FollowException {

		if (userId == null) {

			throw new FollowException("User Id cannot be null", HttpStatus.BAD_REQUEST);
		}

		List<SearchHistory> histories = searchHistoryRepository.findByUserIdOrderBySearchedAtDesc(userId);

		if (histories.isEmpty()) {

			throw new FollowException("Search history not found", HttpStatus.NOT_FOUND);
		}

		return histories.stream().map(history -> modelMapper.map(history, SearchHistoryDTO.class)).toList();
	}

	@Override
	public void clearSearchHistory(Long userId) throws FollowException {

		if (userId == null) {

			throw new FollowException("User Id cannot be null", HttpStatus.BAD_REQUEST);
		}

		List<SearchHistory> histories = searchHistoryRepository.findByUserIdOrderBySearchedAtDesc(userId);

		if (histories.isEmpty()) {

			throw new FollowException("No search history found to clear", HttpStatus.NOT_FOUND);
		}

		searchHistoryRepository.deleteByUserId(userId);
	}
}
