package com.infy.instagram.trendingposts.utility;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import com.infy.instagram.trendingposts.dto.HashtagAnalyticsDto;
import com.infy.instagram.trendingposts.dto.PostAnalyticsDto;

@Component
@RequiredArgsConstructor
public class TrendScoreCalculator {

    private static final double POST_LIKES_WEIGHT = 0.4;
    private static final double POST_COMMENTS_WEIGHT = 0.3;
    private static final double POST_VIEWS_WEIGHT = 0.2;
    private static final double POST_RECENCY_WEIGHT = 0.1;

    private static final double HASHTAG_USAGE_WEIGHT = 0.7;
    private static final double HASHTAG_RECENCY_WEIGHT = 0.3;

    private static final double MAX_POST_RECENCY_BOOST = 100.0;
    private static final double MAX_HASHTAG_RECENCY_BOOST = 50.0;

    private static final double ROUNDING_FACTOR = 100.0;

    private final Clock clock;

    public double calculatePostScore(PostAnalyticsDto analytics) {
        long likes = safe(analytics.getTotalLikes());
        long comments = safe(analytics.getTotalComments());
        long views = safe(analytics.getTotalViews());

        double recencyBoost =
                recencyBoost(analytics.getCreatedAt(), MAX_POST_RECENCY_BOOST);

        return round(
                likes * POST_LIKES_WEIGHT
                        + comments * POST_COMMENTS_WEIGHT
                        + views * POST_VIEWS_WEIGHT
                        + recencyBoost * POST_RECENCY_WEIGHT
        );
    }

    public double calculateHashtagScore(HashtagAnalyticsDto analytics) {
        long usage = safe(analytics.getUsageCount());

        double recencyBoost =
                recencyBoost(analytics.getLastUsedAt(), MAX_HASHTAG_RECENCY_BOOST);

        return round(
                usage * HASHTAG_USAGE_WEIGHT
                        + recencyBoost * HASHTAG_RECENCY_WEIGHT
        );
    }

    private long safe(Long value) {
        return value == null ? 0L : value;
    }

    private double recencyBoost(LocalDateTime dateTime, double maxBoost) {
        if (dateTime == null) {
            return 0.0;
        }

        long hours = Duration.between(dateTime, LocalDateTime.now(clock)).toHours();

        return Math.max(maxBoost - hours, 0.0);
    }

    private double round(double value) {
        return Math.round(value * ROUNDING_FACTOR) / ROUNDING_FACTOR;
    }
}