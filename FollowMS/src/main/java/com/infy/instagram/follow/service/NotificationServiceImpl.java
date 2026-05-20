package com.infy.instagram.follow.service;

import com.infy.instagram.follow.dto.NotificationDTO;
import com.infy.instagram.follow.entity.Notification;
import com.infy.instagram.follow.exception.FollowException;
import com.infy.instagram.follow.repository.NotificationRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationServiceImpl implements NotificationService {

	private final NotificationRepository notificationRepository;

	private final ModelMapper modelMapper;

	@Override
	public List<NotificationDTO> getNotifications(Long userId) {

		return notificationRepository.findByReceiverIdOrderByCreatedAtDesc(userId).stream()
				.map(notification -> modelMapper.map(notification, NotificationDTO.class)).toList();
	}

	@Override
	public NotificationDTO markAsRead(Long id) throws FollowException{

		Notification notification = notificationRepository.findById(id)
				.orElseThrow(() -> new FollowException("Notification not found"));

		notification.setIsRead(true);

		notificationRepository.save(notification);

		return modelMapper.map(notification, NotificationDTO.class);
	}

	@Override
	public void deleteNotification(Long id) {

		notificationRepository.deleteById(id);
	}
}
