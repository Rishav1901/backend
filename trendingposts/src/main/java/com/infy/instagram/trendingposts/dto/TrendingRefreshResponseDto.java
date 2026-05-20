package com.infy.instagram.trendingposts.dto;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrendingRefreshResponseDto {
    private String message;
    private int totalPostsUpdated;
    private int totalHashtagsUpdated;
    private Instant timestamp;
}