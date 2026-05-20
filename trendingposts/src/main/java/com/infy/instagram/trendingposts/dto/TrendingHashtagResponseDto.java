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
public class TrendingHashtagResponseDto {
    private Long hashtagId;
    private String tag;
    private Double trendingScore;
    private Long usageCount;
    private LocalDateTime calculatedAt;
}