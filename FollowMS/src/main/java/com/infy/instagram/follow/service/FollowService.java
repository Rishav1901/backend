package com.infy.instagram.follow.service;

import java.util.List;

import com.infy.instagram.follow.dto.FollowRequestDTO;
import com.infy.instagram.follow.dto.FollowResponseDTO;
import com.infy.instagram.follow.dto.FollowStatusDTO;
import com.infy.instagram.follow.dto.FollowersCountDTO;
import com.infy.instagram.follow.exception.FollowException;

public interface FollowService {

	FollowResponseDTO followUser(FollowRequestDTO request) throws FollowException;

	FollowResponseDTO unfollowUser(FollowRequestDTO request) throws FollowException;

	FollowStatusDTO checkFollowing(Long followerId, Long followingId);

	FollowersCountDTO getCounts(Long userId);

	List<Long> getFollowers(Long userId);

	List<Long> getFollowing(Long userId);

	void blockUser(Long blockerId, Long blockedId) throws FollowException;

	void unblockUser(Long blockerId, Long blockedId) throws FollowException;

	List<Long> getBlockedUsers(Long blockerId);
}
