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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "trending_posts")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrendingPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trending_post_id")
    private Long trendingPostId;

    @Column(name = "post_id", nullable = false, unique = true)
    private Long postId;

    @Column(name = "trending_score", nullable = false)
    private Double trendingScore;

    @Column(name = "total_likes")
    private Long totalLikes;

    @Column(name = "total_comments")
    private Long totalComments;

    @Column(name = "total_views")
    private Long totalViews;

    @Column(name = "calculated_at")
    private LocalDateTime calculatedAt;
}