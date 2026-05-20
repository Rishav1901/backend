package com.infy.instagram.trendingposts.client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.infy.instagram.trendingposts.config.TrendingExternalProperties;
import com.infy.instagram.trendingposts.dto.HashtagAnalyticsDto;
import com.infy.instagram.trendingposts.dto.PostAnalyticsDto;

@Component
@RequiredArgsConstructor
public class PostAnalyticsClient {
	
    private final WebClient.Builder webClientBuilder;
    private final TrendingExternalProperties properties;

    @CircuitBreaker(name = "postService", fallbackMethod = "fallbackPostAnalytics")
    public List<PostAnalyticsDto> fetchPostAnalytics() {
    	System.out.println("The api i am calling " + properties.getBaseUrl() + properties.getPostsAnalyticsPath());
    	List<PostAnalyticsDto> response = new ArrayList<>();
    	try {
//    	 WebClient client = WebClient.create();
//    	 response = client.get()
//    			 .uri("http://localhost:8081/posts")
// 			    .retrieve()
// 			    .bodyToFlux(PostAnalyticsDto.class)
// 			    .collectList()
// 			    .block();
    	 response = webClientBuilder.build()
    			    .get()
    			    .uri(properties.getBaseUrl() + properties.getPostsAnalyticsPath())
    			    .retrieve()
    			    .bodyToFlux(PostAnalyticsDto.class)
    			    .collectList()
    			    .block();
    	}catch(Exception e) {
    		System.out.println("The error I am getting: " + e);
    	}

        return response;
    }

    @CircuitBreaker(name = "postService", fallbackMethod = "fallbackHashtagAnalytics")
    public List<HashtagAnalyticsDto> fetchHashtagAnalytics() {
        HashtagAnalyticsDto[] response = webClientBuilder.build()
                .get()
                .uri(properties.getBaseUrl() + properties.getHashtagsAnalyticsPath())
                .retrieve()
                .bodyToMono(HashtagAnalyticsDto[].class)
                .block(Duration.ofSeconds(3));

        return response == null ? List.of() : Arrays.asList(response);
    }

    @SuppressWarnings("unused")
    private List<PostAnalyticsDto> fallbackPostAnalytics(Throwable throwable) {
        return List.of();
    }

    @SuppressWarnings("unused")
    private List<HashtagAnalyticsDto> fallbackHashtagAnalytics(Throwable throwable) {
        return List.of();
    }
}