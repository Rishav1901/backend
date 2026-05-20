package com.infy.instagram.trendingposts.repository;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.infy.instagram.trendingposts.entity.TrendingPost;

public interface TrendingPostRepository extends JpaRepository<TrendingPost, Long> {
    List<TrendingPost> findAllByOrderByTrendingScoreDesc(Pageable pageable);
    List<TrendingPost> findAllByOrderByCalculatedAtDesc(Pageable pageable);
    List<TrendingPost> findAllByOrderByTotalLikesDesc(Pageable pageable);
    List<TrendingPost> findAllByOrderByTotalCommentsDesc(Pageable pageable);
    List<TrendingPost> findAllByOrderByTotalViewsDesc(Pageable pageable);
}