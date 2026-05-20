package com.infy.instagram.follow.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FollowRequestDTO {

   @NotNull(
           message =
           "Please provide a valid followerId"
   )
   @Positive(
           message =
           "Please provide a valid followerId"
   )
   private Long followerId;

   @NotNull(
           message =
           "Please provide a valid followingId"
   )
   @Positive(
           message =
           "Please provide a valid followingId"
   )
   private Long followingId;
}
