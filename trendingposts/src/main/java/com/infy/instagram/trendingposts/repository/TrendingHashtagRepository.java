package com.infy.instagram.trendingposts.repository;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.infy.instagram.trendingposts.entity.TrendingHashtag;

public interface TrendingHashtagRepository extends JpaRepository<TrendingHashtag, Long> {
    List<TrendingHashtag> findAllByOrderByTrendingScoreDesc(Pageable pageable);
    List<TrendingHashtag> findAllByOrderByCalculatedAtDesc(Pageable pageable);
    List<TrendingHashtag> findAllByOrderByUsageCountDesc(Pageable pageable);
}