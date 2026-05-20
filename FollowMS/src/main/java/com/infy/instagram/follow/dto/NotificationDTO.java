package com.infy.instagram.follow.dto;

import lombok.*;

import java.time.LocalDateTime;

import com.infy.instagram.follow.enums.NotificationType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationDTO {

   private Long id;

   private Long senderId;

   private Long receiverId;

   private String message;

   private NotificationType type;

   private Boolean isRead;

   private LocalDateTime createdAt;
}
