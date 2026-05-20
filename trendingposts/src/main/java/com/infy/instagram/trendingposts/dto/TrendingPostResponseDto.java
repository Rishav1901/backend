package com.infy.instagram.trendingposts.dto;

import java.time.LocalDateTime;
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
public class TrendingPostResponseDto {
    private Long postId;
    private Double trendingScore;
    private Long totalLikes;
    private Long totalComments;
    private Long totalViews;
    private LocalDateTime calculatedAt;
}