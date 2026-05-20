package com.infy.instagram.follow.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import com.infy.instagram.follow.enums.NotificationType;

@Entity
@Table(name = "notifications",

		indexes = {

				@Index(name = "idx_receiver", columnList = "receiver_id"),

				@Index(name = "idx_sender", columnList = "sender_id") })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "sender_id", nullable = false)
	private Long senderId;

	@Column(name = "receiver_id", nullable = false)
	private Long receiverId;

	@Column(nullable = false, length = 500)
	private String message;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private NotificationType type;

	@Column(name = "is_read", nullable = false)
	private Boolean isRead;

	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@PrePersist
	public void prePersist() {

		this.createdAt = LocalDateTime.now();

		if (this.isRead == null) {

			this.isRead = false;
		}
	}
}