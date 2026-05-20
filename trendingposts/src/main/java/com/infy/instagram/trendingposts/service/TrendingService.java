package com.infy.instagram.trendingposts.service;

import java.util.List;

import com.infy.instagram.trendingposts.dto.TrendingHashtagResponseDto;
import com.infy.instagram.trendingposts.dto.TrendingPostResponseDto;
import com.infy.instagram.trendingposts.dto.TrendingRefreshResponseDto;
import com.infy.instagram.trendingposts.dto.TrendingSortBy;

public interface TrendingService {
    List<TrendingPostResponseDto> getTrendingPosts(int limit, TrendingSortBy sortBy);
    List<TrendingHashtagResponseDto> getTrendingHashtags(int limit, TrendingSortBy sortBy);
    TrendingRefreshResponseDto recalculateAllTrendingData();
    TrendingRefreshResponseDto recalculateTrendingPosts();
    TrendingRefreshResponseDto recalculateTrendingHashtags();
    List<Long> getTrendingPostsIds(int limit, TrendingSortBy sortBy);
    List<Long> getTrendingHashtagsIds(int limit, TrendingSortBy sortBy);
}