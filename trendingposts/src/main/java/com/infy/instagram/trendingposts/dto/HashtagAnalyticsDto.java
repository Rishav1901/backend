package com.infy.instagram.trendingposts.dto;

import java.time.LocalDateTime;
import jakarta.validation.constraints.NotNull;
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
public class HashtagAnalyticsDto {
    @NotNull
    private Long hashtagId;
    private String tag;
    private Long usageCount;
    @NotNull
    private LocalDateTime lastUsedAt;
}