package com.infy.instagram.follow.api;

import com.infy.instagram.follow.dto.FollowRequestDTO;
import com.infy.instagram.follow.dto.FollowResponseDTO;
import com.infy.instagram.follow.dto.FollowStatusDTO;
import com.infy.instagram.follow.dto.FollowersCountDTO;
import com.infy.instagram.follow.exception.FollowException;
import com.infy.instagram.follow.service.FollowService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/follows")
@Validated
@RequiredArgsConstructor
public class FollowController {

	private final FollowService followService;

	@PostMapping
	public ResponseEntity<FollowResponseDTO> followUser(@Valid @RequestBody FollowRequestDTO request)
			throws FollowException {

		FollowResponseDTO responseDTO = followService.followUser(request);

		return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
	}

	@DeleteMapping
	public ResponseEntity<FollowResponseDTO> unfollowUser(@Valid @RequestBody FollowRequestDTO request)
			throws FollowException {

		FollowResponseDTO responseDTO = followService.unfollowUser(request);

		return new ResponseEntity<>(responseDTO, HttpStatus.OK);
	}

	@GetMapping("/check")
	public ResponseEntity<FollowStatusDTO> checkFollowing(
			@RequestParam @NotNull @Positive Long followerId,
			@RequestParam @NotNull @Positive Long followingId) {

		FollowStatusDTO responseDTO = followService.checkFollowing(followerId, followingId);

		return new ResponseEntity<>(responseDTO, HttpStatus.OK);
	}

	@GetMapping("/count/{userId}")
	public ResponseEntity<FollowersCountDTO> getCounts(@PathVariable @NotNull @Positive Long userId) {

		FollowersCountDTO responseDTO = followService.getCounts(userId);

		return new ResponseEntity<>(responseDTO, HttpStatus.OK);
	}

	@GetMapping("/followers/{userId}")
	public ResponseEntity<List<Long>> getFollowers(@PathVariable @NotNull @Positive Long userId) {

		List<Long> responseDTO = followService.getFollowers(userId);

		return new ResponseEntity<>(responseDTO, HttpStatus.OK);
	}

	@GetMapping("/following/{userId}")
	public ResponseEntity<List<Long>> getFollowing(@PathVariable @NotNull @Positive Long userId) {

		List<Long> responseDTO = followService.getFollowing(userId);

		return new ResponseEntity<>(responseDTO, HttpStatus.OK);
	}

	@PostMapping("/block")
	public ResponseEntity<String> blockUser(@RequestHeader("X-User-Id") @NotNull @Positive Long blockerId,
			@RequestParam @NotNull @Positive Long blockedId) throws FollowException {

		followService.blockUser(blockerId, blockedId);

		return new ResponseEntity<>("User blocked successfully", HttpStatus.CREATED);
	}

	@DeleteMapping("/block")
	public ResponseEntity<String> unblockUser(@RequestHeader("X-User-Id") @NotNull @Positive Long blockerId,
			@RequestParam @NotNull @Positive Long blockedId) throws FollowException {

		followService.unblockUser(blockerId, blockedId);

		return new ResponseEntity<>("User unblocked successfully", HttpStatus.OK);
	}

	@GetMapping("/blocked")
	public ResponseEntity<List<Long>> getBlockedUsers(@RequestHeader("X-User-Id") @NotNull @Positive Long blockerId) {

		List<Long> blockedUsers = followService.getBlockedUsers(blockerId);

		return new ResponseEntity<>(blockedUsers, HttpStatus.OK);
	}
}