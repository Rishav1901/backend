package com.infy.instagram.trendingposts.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "trending_hashtags")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrendingHashtag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trending_hashtag_id")
    private Long trendingHashtagId;

    @Column(name = "hashtag_id", nullable = false, unique = true)
    private Long hashtagId;

    @Column(name = "tag")
    private String tag;

    @Column(name = "trending_score", nullable = false)
    private Double trendingScore;

    @Column(name = "usage_count")
    private Long usageCount;

    @Column(name = "calculated_at")
    private LocalDateTime calculatedAt;
}