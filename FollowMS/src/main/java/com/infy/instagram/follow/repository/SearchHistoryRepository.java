package com.infy.instagram.follow.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.infy.instagram.follow.entity.SearchHistory;

import java.util.List;

public interface SearchHistoryRepository extends JpaRepository<SearchHistory, Long> {

	List<SearchHistory> findByUserIdOrderBySearchedAtDesc(Long userId);

	void deleteByUserId(Long userId);
}