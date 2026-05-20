package com.infy.instagram.follow.service;

import com.infy.instagram.follow.dto.*;
import com.infy.instagram.follow.entity.Follow;
import com.infy.instagram.follow.entity.Notification;
import com.infy.instagram.follow.entity.BlockedUser;
import com.infy.instagram.follow.enums.NotificationType;
import com.infy.instagram.follow.exception.FollowException;
import com.infy.instagram.follow.repository.FollowRepository;
import com.infy.instagram.follow.repository.NotificationRepository;
import com.infy.instagram.follow.repository.BlockedUserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class FollowServiceImpl implements FollowService {

	private final FollowRepository followRepository;

	private final NotificationRepository notificationRepository;

	private final BlockedUserRepository blockedUserRepository;

	private final WebClient.Builder webClientBuilder;

	@Override
	public FollowResponseDTO followUser(FollowRequestDTO request) throws FollowException {
		ensureUserExists(request.getFollowerId());
		ensureUserExists(request.getFollowingId());

		if (request.getFollowerId().equals(request.getFollowingId())) {

			throw new FollowException("User cannot follow himself", HttpStatus.BAD_REQUEST);
		}

		boolean alreadyFollowing = followRepository.existsByFollowerIdAndFollowingId(request.getFollowerId(),
				request.getFollowingId());

		if (alreadyFollowing) {

			throw new FollowException("Already following this user", HttpStatus.CONFLICT);
		}

		Follow follow = Follow.builder().followerId(request.getFollowerId()).followingId(request.getFollowingId())
				.build();

		followRepository.save(follow);

		Notification notification = Notification.builder().senderId(request.getFollowerId())
				.receiverId(request.getFollowingId()).message("Started following you").type(NotificationType.FOLLOW)
				.build();

		notificationRepository.save(notification);

		return FollowResponseDTO.builder().message("User followed successfully").success(true).build();
	}

	@Override
	public FollowResponseDTO unfollowUser(FollowRequestDTO request) throws FollowException {

		boolean exists = followRepository.existsByFollowerIdAndFollowingId(request.getFollowerId(),
				request.getFollowingId());

		if (!exists) {

			throw new FollowException("Follow relation not found", HttpStatus.NOT_FOUND);
		}

		followRepository.deleteByFollowerIdAndFollowingId(request.getFollowerId(), request.getFollowingId());

		return FollowResponseDTO.builder().message("User unfollowed successfully").success(true).build();
	}

	private void ensureUserExists(Long userId) throws FollowException {
		try {
			webClientBuilder.build().get().uri("http://AuthenticationMS/api/users/userId/{userId}", userId)
					.retrieve().toBodilessEntity().block();
		} catch (WebClientResponseException.NotFound exception) {
			throw new FollowException("User not found", HttpStatus.NOT_FOUND);
		} catch (WebClientResponseException exception) {
			throw new FollowException("Unable to verify user", HttpStatus.SERVICE_UNAVAILABLE);
		} catch (Exception exception) {
			throw new FollowException("Unable to verify user", HttpStatus.SERVICE_UNAVAILABLE);
		}
	}

	@Override
	public FollowStatusDTO checkFollowing(Long followerId, Long followingId) {

		boolean following = followRepository.existsByFollowerIdAndFollowingId(followerId, followingId);

		return FollowStatusDTO.builder().following(following).build();
	}

	@Override
	public FollowersCountDTO getCounts(Long userId) {

		long followers = followRepository.countByFollowingId(userId);

		long following = followRepository.countByFollowerId(userId);

		return FollowersCountDTO.builder().userId(userId).followersCount(followers).followingCount(following).build();
	}

	@Override
	public List<Long> getFollowers(Long userId) {

		return followRepository.findByFollowingId(userId).stream().map(Follow::getFollowerId).toList();
	}

	@Override
	public List<Long> getFollowing(Long userId) {

		return followRepository.findByFollowerId(userId).stream().map(Follow::getFollowingId).toList();
	}

	@Override
	public void blockUser(Long blockerId, Long blockedId) throws FollowException {
		if (blockerId.equals(blockedId)) {
			throw new FollowException("Cannot block yourself", HttpStatus.BAD_REQUEST);
		}

		boolean alreadyBlocked = blockedUserRepository.existsByBlockerIdAndBlockedId(blockerId, blockedId);
		if (alreadyBlocked) {
			throw new FollowException("User already blocked", HttpStatus.BAD_REQUEST);
		}

		BlockedUser blockedUser = new BlockedUser();
		blockedUser.setBlockerId(blockerId);
		blockedUser.setBlockedId(blockedId);
		blockedUser.setBlockedAt(LocalDateTime.now());
		blockedUserRepository.save(blockedUser);

		// Auto-unfollow if following
		followRepository.deleteByFollowerIdAndFollowingId(blockerId, blockedId);
	}

	@Override
	public void unblockUser(Long blockerId, Long blockedId) throws FollowException {
		blockedUserRepository.deleteByBlockerIdAndBlockedId(blockerId, blockedId);
	}

	@Override
	public List<Long> getBlockedUsers(Long blockerId) {
		return blockedUserRepository.findByBlockerId(blockerId).stream()
				.map(BlockedUser::getBlockedId)
				.toList();
	}
}
