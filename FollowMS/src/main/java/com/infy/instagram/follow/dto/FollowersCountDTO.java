package com.infy.instagram.follow.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FollowersCountDTO {

   private Long userId;

   private Long followersCount;

   private Long followingCount;
}