package com.infy.instagram.trendingposts.exception;

public class TrendingServiceException extends RuntimeException {
    public TrendingServiceException(String message) {
        super(message);
    }

    public TrendingServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}