package com.infy.instagram.trendingposts.service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.infy.instagram.trendingposts.repository.TrendingPostRepository;
import com.infy.instagram.trendingposts.utility.TrendScoreCalculator;
import com.infy.instagram.trendingposts.client.PostAnalyticsClient;
import com.infy.instagram.trendingposts.dto.HashtagAnalyticsDto;
import com.infy.instagram.trendingposts.dto.PostAnalyticsDto;
import com.infy.instagram.trendingposts.dto.TrendingHashtagResponseDto;
import com.infy.instagram.trendingposts.dto.TrendingPostResponseDto;
import com.infy.instagram.trendingposts.dto.TrendingRefreshResponseDto;
import com.infy.instagram.trendingposts.dto.TrendingSortBy;
import com.infy.instagram.trendingposts.entity.TrendingHashtag;
import com.infy.instagram.trendingposts.entity.TrendingPost;
import com.infy.instagram.trendingposts.exception.DataNotFoundException;
import com.infy.instagram.trendingposts.exception.TrendingServiceException;
import com.infy.instagram.trendingposts.mapper.TrendingMapper;
import com.infy.instagram.trendingposts.repository.TrendingHashtagRepository;

@Service
@RequiredArgsConstructor
public class TrendingServiceImpl implements TrendingService {

    private final TrendingPostRepository trendingPostRepository;
    private final TrendingHashtagRepository trendingHashtagRepository;
    private final PostAnalyticsClient postAnalyticsClient;
    private final TrendScoreCalculator trendScoreCalculator;
    private final TrendingMapper trendingMapper;
    private final Clock clock;

    @Override
    public List<TrendingPostResponseDto> getTrendingPosts(int limit, TrendingSortBy sortBy) {
        validateLimit(limit);
        List<TrendingPost> posts = loadTrendingPosts(limit, sortBy);
        if (posts.isEmpty()) {
            recalculateTrendingPosts();
            posts = loadTrendingPosts(limit, sortBy);
        }
        return posts.stream().map(trendingMapper::toTrendingPostResponseDto).toList();
    }
    
    @Override
    public List<Long> getTrendingPostsIds(int limit, TrendingSortBy sortBy){
    	validateLimit(limit);
        List<TrendingPost> posts = loadTrendingPosts(limit, sortBy);
        if (posts.isEmpty()) {
            recalculateTrendingPosts();
            posts = loadTrendingPosts(limit, sortBy);
        }
        return posts.stream().map(p -> p.getPostId()).toList();
    }

    @Override
    public List<TrendingHashtagResponseDto> getTrendingHashtags(int limit, TrendingSortBy sortBy) {
        validateLimit(limit);
        List<TrendingHashtag> hashtags = loadTrendingHashtags(limit, sortBy);
        if (hashtags.isEmpty()) {
            recalculateTrendingHashtags();
            hashtags = loadTrendingHashtags(limit, sortBy);
        }
        return hashtags.stream().map(trendingMapper::toTrendingHashtagResponseDto).toList();
    }
    
    
    @Override
    public List<Long> getTrendingHashtagsIds(int limit, TrendingSortBy sortBy) {
    	validateLimit(limit);
        List<TrendingHashtag> hashtags = loadTrendingHashtags(limit, sortBy);
        if (hashtags.isEmpty()) {
            recalculateTrendingHashtags();
            hashtags = loadTrendingHashtags(limit, sortBy);
        }
        return hashtags.stream().map(h -> h.getHashtagId()).toList();
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = {"trendingPosts", "trendingHashtags"}, allEntries = true)
    public TrendingRefreshResponseDto recalculateAllTrendingData() {
        TrendingRefreshResponseDto posts = recalculateTrendingPosts();
        TrendingRefreshResponseDto hashtags = recalculateTrendingHashtags();

        return TrendingRefreshResponseDto.builder()
                .message("Trending posts and hashtags refreshed successfully")
                .totalPostsUpdated(posts.getTotalPostsUpdated())
                .totalHashtagsUpdated(hashtags.getTotalHashtagsUpdated())
                .timestamp(java.time.Instant.now(clock))
                .build();
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = "trendingPosts", allEntries = true)
    public TrendingRefreshResponseDto recalculateTrendingPosts() {
        List<PostAnalyticsDto> analytics = postAnalyticsClient.fetchPostAnalytics();
        if (analytics.isEmpty()) {
            throw new DataNotFoundException("No post analytics available to calculate trending posts");
        }

        List<TrendingPost> entities = analytics.stream()
                .map(this::toTrendingPostEntity)
                .sorted(Comparator.comparing(TrendingPost::getTrendingScore).reversed())
                .toList();

        trendingPostRepository.deleteAllInBatch();
        trendingPostRepository.flush();
        trendingPostRepository.saveAll(entities);

        return TrendingRefreshResponseDto.builder()
                .message("Trending posts refreshed successfully")
                .totalPostsUpdated(entities.size())
                .totalHashtagsUpdated(0)
                .timestamp(java.time.Instant.now(clock))
                .build();
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = "trendingHashtags", allEntries = true)
    public TrendingRefreshResponseDto recalculateTrendingHashtags() {
        List<HashtagAnalyticsDto> analytics = postAnalyticsClient.fetchHashtagAnalytics();
        if (analytics.isEmpty()) {
            throw new DataNotFoundException("No hashtag analytics available to calculate trending hashtags");
        }

        List<TrendingHashtag> entities = analytics.stream()
                .map(this::toTrendingHashtagEntity)
                .sorted(Comparator.comparing(TrendingHashtag::getTrendingScore).reversed())
                .toList();

        trendingHashtagRepository.deleteAllInBatch();
        trendingHashtagRepository.flush();
        trendingHashtagRepository.saveAll(entities);

        return TrendingRefreshResponseDto.builder()
                .message("Trending hashtags refreshed successfully")
                .totalPostsUpdated(0)
                .totalHashtagsUpdated(entities.size())
                .timestamp(java.time.Instant.now(clock))
                .build();
    }

    private TrendingPost toTrendingPostEntity(PostAnalyticsDto analytics) {
        return TrendingPost.builder()
                .postId(analytics.getPostId())
                .totalLikes(safe(analytics.getTotalLikes()))
                .totalComments(safe(analytics.getTotalComments()))
                .totalViews(safe(analytics.getTotalViews()))
                .trendingScore(trendScoreCalculator.calculatePostScore(analytics))
                .calculatedAt(LocalDateTime.now(clock))
                .build();
    }

    private TrendingHashtag toTrendingHashtagEntity(HashtagAnalyticsDto analytics) {
        return TrendingHashtag.builder()
                .hashtagId(analytics.getHashtagId())
                .tag(analytics.getTag())
                .usageCount(safe(analytics.getUsageCount()))
                .trendingScore(trendScoreCalculator.calculateHashtagScore(analytics))
                .calculatedAt(LocalDateTime.now(clock))
                .build();
    }

    private List<TrendingPost> loadTrendingPosts(int limit, TrendingSortBy sortBy) {
        PageRequest pageable = PageRequest.of(0, limit);
        return switch (sortBy) {
            case RECENCY -> trendingPostRepository.findAllByOrderByCalculatedAtDesc(pageable);
            case LIKES -> trendingPostRepository.findAllByOrderByTotalLikesDesc(pageable);
            case COMMENTS -> trendingPostRepository.findAllByOrderByTotalCommentsDesc(pageable);
            case VIEWS -> trendingPostRepository.findAllByOrderByTotalViewsDesc(pageable);
            case TRENDING_SCORE, USAGE_COUNT -> trendingPostRepository.findAllByOrderByTrendingScoreDesc(pageable);
        };
    }

    private List<TrendingHashtag> loadTrendingHashtags(int limit, TrendingSortBy sortBy) {
        PageRequest pageable = PageRequest.of(0, limit);
        return switch (sortBy) {
            case RECENCY -> trendingHashtagRepository.findAllByOrderByCalculatedAtDesc(pageable);
            case USAGE_COUNT -> trendingHashtagRepository.findAllByOrderByUsageCountDesc(pageable);
            case TRENDING_SCORE, LIKES, COMMENTS, VIEWS -> trendingHashtagRepository.findAllByOrderByTrendingScoreDesc(pageable);
        };
    }

    private long safe(Long value) {
        return value == null ? 0L : value;
    }

    private void validateLimit(int limit) {
        if (limit < 1 || limit > 100) {
            throw new TrendingServiceException("limit must be between 1 and 100");
        }
    }
}