package com.infy.instagram.follow.api;

import com.infy.instagram.follow.dto.NotificationDTO;
import com.infy.instagram.follow.exception.FollowException;
import com.infy.instagram.follow.service.NotificationService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

	private final NotificationService notificationService;

	@GetMapping("/{userId}")
	public ResponseEntity<List<NotificationDTO>> getNotifications(@PathVariable Long userId) throws FollowException {

		List<NotificationDTO> responseDTO = notificationService.getNotifications(userId);

		return new ResponseEntity<>(responseDTO, HttpStatus.OK);
	}

	@PutMapping("/read/{id}")
	public ResponseEntity<NotificationDTO> markAsRead(@PathVariable Long id) throws FollowException {

		NotificationDTO responseDTO = notificationService.markAsRead(id);

		return new ResponseEntity<>(responseDTO, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteNotification(@PathVariable Long id) throws FollowException {

		notificationService.deleteNotification(id);

		return new ResponseEntity<>("Notification deleted successfully", HttpStatus.OK);
	}
}
