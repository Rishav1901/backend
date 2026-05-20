package com.infy.instagram.follow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.infy.instagram.follow.entity.Follow;

import java.util.List;
import java.util.Optional;


@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {

	Optional<Follow> findByFollowerIdAndFollowingId(Long followerId, Long followingId);

	boolean existsByFollowerIdAndFollowingId(Long followerId, Long followingId);

	List<Follow> findByFollowerId(Long followerId);

	List<Follow> findByFollowingId(Long followingId);

	long countByFollowerId(Long followerId);

	long countByFollowingId(Long followingId);

	void deleteByFollowerIdAndFollowingId(Long followerId, Long followingId);
}
