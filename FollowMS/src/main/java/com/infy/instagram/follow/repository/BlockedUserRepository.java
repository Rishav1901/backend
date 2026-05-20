package com.infy.instagram.follow.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.infy.instagram.follow.entity.BlockedUser;

@Repository
public interface BlockedUserRepository extends JpaRepository<BlockedUser, Long> {
	List<BlockedUser> findByBlockerId(Long blockerId);

	boolean existsByBlockerIdAndBlockedId(Long blockerId, Long blockedId);

	void deleteByBlockerIdAndBlockedId(Long blockerId, Long blockedId);
}
