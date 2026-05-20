package com.infy.instagram.follow.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FollowerUpdateDTO {

   private Long userId;

   private Long followersCount;
}
