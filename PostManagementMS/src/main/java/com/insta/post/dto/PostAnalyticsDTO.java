package com.insta.post.dto;

import java.time.LocalDateTime;
import java.util.List;

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
public class PostAnalyticsDTO {
    @NotNull
    private Long postId;
    private Long totalLikes;
    private Long totalComments;
    private Long totalViews;
    private Long totalSaves;
    private LocalDateTime postCreatedAt;
    private List<String> hashtags;
    @NotNull
    private LocalDateTime createdAt;
}