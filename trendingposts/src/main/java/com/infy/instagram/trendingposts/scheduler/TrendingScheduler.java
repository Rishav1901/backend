package com.infy.instagram.trendingposts.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.infy.instagram.trendingposts.service.TrendingService;

@Component
@RequiredArgsConstructor
@Slf4j
public class TrendingScheduler {

    private final TrendingService trendingService;

    @Scheduled(fixedRate = 60000)
    public void refreshTrendingData() {
        log.info("Starting scheduled trending refresh");
        try {
            trendingService.recalculateAllTrendingData();
            log.info("Scheduled trending refresh completed successfully");
        } catch (Exception ex) {
            log.error("Scheduled trending refresh failed: {}", ex.getMessage(), ex);
        }
    }
}