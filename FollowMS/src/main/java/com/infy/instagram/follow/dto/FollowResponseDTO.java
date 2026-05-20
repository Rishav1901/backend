package com.infy.instagram.follow.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FollowResponseDTO {

   private String message;

   private Boolean success;
}
