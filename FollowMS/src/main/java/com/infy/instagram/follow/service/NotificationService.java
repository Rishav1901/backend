package com.infy.instagram.follow.service;

import java.util.List;

import com.infy.instagram.follow.dto.NotificationDTO;
import com.infy.instagram.follow.exception.FollowException;

public interface NotificationService {

	List<NotificationDTO> getNotifications(Long userId);

	NotificationDTO markAsRead(Long id) throws FollowException;

	void deleteNotification(Long id);
}