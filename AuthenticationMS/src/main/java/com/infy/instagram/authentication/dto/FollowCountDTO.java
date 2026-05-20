package com.infy.instagram.authentication.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FollowCountDTO {
	private Long followersCount;
	private Long followingCount;
}
