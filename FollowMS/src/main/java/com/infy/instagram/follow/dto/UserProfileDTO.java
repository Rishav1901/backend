package com.infy.instagram.follow.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileDTO {

   private Long id;

   private String username;

   private String fullName;

   private String bio;

   private String profilePicture;

   private Long followersCount;

   private Long followingCount;

   private Boolean isFollowing;
}