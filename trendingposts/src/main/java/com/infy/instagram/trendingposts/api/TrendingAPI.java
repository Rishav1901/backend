package com.infy.instagram.trendingposts.api;

import com.infy.instagram.trendingposts.dto.ApiResponse;
import com.infy.instagram.trendingposts.dto.TrendingHashtagResponseDto;
import com.infy.instagram.trendingposts.dto.TrendingPostResponseDto;
import com.infy.instagram.trendingposts.dto.TrendingRefreshResponseDto;
import com.infy.instagram.trendingposts.dto.TrendingSortBy;
import com.infy.instagram.trendingposts.service.TrendingService;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/trending")
@RequiredArgsConstructor
@Validated
public class TrendingAPI {

    private final TrendingService trendingService;

    @GetMapping("/posts")
    public ResponseEntity<ApiResponse<List<TrendingPostResponseDto>>> getTrendingPosts(
            @RequestParam(defaultValue = "20")
            @Min(value = 1, message = "Please provide a valid limit")
            @Max(value = 100, message = "Please provide a valid limit")
            int limit,
            @RequestParam(defaultValue = "TRENDING_SCORE")
            TrendingSortBy sortBy) {
        return ResponseEntity.ok(ApiResponse.<List<TrendingPostResponseDto>>builder()
                .success(true)
                .message("Trending posts fetched successfully")
                .data(trendingService.getTrendingPosts(limit, sortBy))
                .timestamp(Instant.now())
                .build());
    }
    
    @GetMapping("/posts/ids")
    public ResponseEntity<List<Long>> getTrendingPostsIds(
            @RequestParam(defaultValue = "20")
            @Min(value = 1, message = "Please provide a valid limit")
            @Max(value = 100, message = "Please provide a valid limit")
            int limit,
            @RequestParam(defaultValue = "TRENDING_SCORE")
            TrendingSortBy sortBy) {
        return new ResponseEntity<List<Long>>(trendingService.getTrendingPostsIds(limit, sortBy), HttpStatus.OK);
    }

    @GetMapping("/hashtags")
    public ResponseEntity<ApiResponse<List<TrendingHashtagResponseDto>>> getTrendingHashtags(
            @RequestParam(defaultValue = "20")
            @Min(value = 1, message = "Please provide a valid limit")
            @Max(value = 100, message = "Please provide a valid limit")
            int limit,
            @RequestParam(defaultValue = "TRENDING_SCORE")
            TrendingSortBy sortBy) {
        return ResponseEntity.ok(ApiResponse.<List<TrendingHashtagResponseDto>>builder()
                .success(true)
                .message("Trending hashtags fetched successfully")
                .data(trendingService.getTrendingHashtags(limit, sortBy))
                .timestamp(Instant.now())
                .build());
    }
    
    @GetMapping("/hashtags/ids")
    public ResponseEntity<List<Long>> getTrendingHashtagsIds(
            @RequestParam(defaultValue = "20")
            @Min(value = 1, message = "Please provide a valid limit")
            @Max(value = 100, message = "Please provide a valid limit")
            int limit,
            @RequestParam(defaultValue = "TRENDING_SCORE")
            TrendingSortBy sortBy) {
        return new ResponseEntity<List<Long>>(trendingService.getTrendingHashtagsIds(limit, sortBy), HttpStatus.OK);
    }

    @PostMapping("/recalculate")
    public ResponseEntity<ApiResponse<TrendingRefreshResponseDto>> recalculateAll() {
        TrendingRefreshResponseDto response = trendingService.recalculateAllTrendingData();
        return ResponseEntity.ok(ApiResponse.<TrendingRefreshResponseDto>builder()
                .success(true)
                .message(response.getMessage())
                .data(response)
                .timestamp(Instant.now())
                .build());
    }

    @PostMapping("/recalculate/posts")
    public ResponseEntity<ApiResponse<TrendingRefreshResponseDto>> recalculatePosts() {
        TrendingRefreshResponseDto response = trendingService.recalculateTrendingPosts();
        return ResponseEntity.ok(ApiResponse.<TrendingRefreshResponseDto>builder()
                .success(true)
                .message(response.getMessage())
                .data(response)
                .timestamp(Instant.now())
                .build());
    }

    @PostMapping("/recalculate/hashtags")
    public ResponseEntity<ApiResponse<TrendingRefreshResponseDto>> recalculateHashtags() {
        TrendingRefreshResponseDto response = trendingService.recalculateTrendingHashtags();
        return ResponseEntity.ok(ApiResponse.<TrendingRefreshResponseDto>builder()
                .success(true)
                .message(response.getMessage())
                .data(response)
                .timestamp(Instant.now())
                .build());
    }
}