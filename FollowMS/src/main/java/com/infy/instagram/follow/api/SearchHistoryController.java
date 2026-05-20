package com.infy.instagram.follow.api;

import com.infy.instagram.follow.dto.SearchHistoryDTO;
import com.infy.instagram.follow.exception.FollowException;
import com.infy.instagram.follow.service.SearchHistoryService;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/search")
@Validated
@RequiredArgsConstructor
public class SearchHistoryController {

	private final SearchHistoryService searchHistoryService;

	@PostMapping("/history")
	public ResponseEntity<String> saveSearchHistory(@RequestParam @NotNull @Positive Long userId,
			@RequestParam(required = false) Long searchedUserId, @RequestParam(required = false) String keyword)
			throws FollowException {

		searchHistoryService.saveSearchHistory(userId, searchedUserId, keyword);

		return new ResponseEntity<>("Search history saved successfully", HttpStatus.CREATED);
	}

	@GetMapping("/history/{userId}")
	public ResponseEntity<List<SearchHistoryDTO>> getSearchHistory(@PathVariable @NotNull @Positive Long userId)
			throws FollowException {

		List<SearchHistoryDTO> responseDTO = searchHistoryService.getSearchHistory(userId);

		return new ResponseEntity<>(responseDTO, HttpStatus.OK);
	}

	@DeleteMapping("/history/{userId}")
	public ResponseEntity<String> clearHistory(@PathVariable @NotNull @Positive Long userId) throws FollowException {

		searchHistoryService.clearSearchHistory(userId);

		return new ResponseEntity<>("Search history cleared successfully", HttpStatus.OK);
	}
}