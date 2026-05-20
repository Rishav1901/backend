package com.infy.instagram.follow.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchHistoryDTO {

   private Long id;

   private Long userId;

   private Long searchedUserId;

   private String searchedKeyword;

   private LocalDateTime searchedAt;
}
