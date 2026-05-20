package com.infy.instagram.follow.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;

@Entity
@Table(name = "blocked_users", uniqueConstraints = {
	@UniqueConstraint(columnNames = {"blocker_id", "blocked_id"})
})
@Data
public class BlockedUser {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long blockId;

	@Column(name = "blocker_id", nullable = false)
	private Long blockerId;

	@Column(name = "blocked_id", nullable = false)
	private Long blockedId;

	private LocalDateTime blockedAt;
}
