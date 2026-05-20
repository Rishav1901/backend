package com.infy.instagram.follow.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "search_history",

		indexes = {

				@Index(name = "idx_search_user", columnList = "user_id") })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchHistory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "user_id", nullable = false)
	private Long userId;

	@Column(name = "searched_user_id")
	private Long searchedUserId;

	@Column(name = "searched_keyword", nullable = false, length = 100)
	private String searchedKeyword;

	@Column(name = "searched_at", nullable = false, updatable = false)
	private LocalDateTime searchedAt;

	@PrePersist
	public void prePersist() {

		this.searchedAt = LocalDateTime.now();
	}
}