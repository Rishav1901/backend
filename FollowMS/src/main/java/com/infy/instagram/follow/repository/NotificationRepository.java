package com.infy.instagram.follow.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.infy.instagram.follow.entity.Notification;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

	List<Notification> findByReceiverIdOrderByCreatedAtDesc(Long receiverId);
}
