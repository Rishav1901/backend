package com.infy.instagram.trendingposts.utility;

import java.time.Instant;

import com.infy.instagram.trendingposts.dto.ApiResponse;

public final class ResponseBuilder {

    private ResponseBuilder() {
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .timestamp(Instant.now())
                .build();
    }
}