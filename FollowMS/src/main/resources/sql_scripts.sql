-- Instagram Follow System Database Schema
CREATE DATABASE IF NOT EXISTS follow_db;

-- 1. Follows Table
CREATE TABLE follows (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    follower_id BIGINT NOT NULL,
    following_id BIGINT NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    UNIQUE KEY uk_follower_following (follower_id, following_id),
    INDEX idx_follower (follower_id),
    INDEX idx_following (following_id)
);

-- 2. Notifications Table
CREATE TABLE notifications (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    sender_id BIGINT NOT NULL,
    receiver_id BIGINT NOT NULL,
    message VARCHAR(500) NOT NULL,
    type VARCHAR(50) NOT NULL,
    is_read BOOLEAN NOT NULL DEFAULT FALSE,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    INDEX idx_receiver (receiver_id),
    INDEX idx_sender (sender_id)
);

-- 3. Search History Table
CREATE TABLE search_history (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    searched_user_id BIGINT,
    searched_keyword VARCHAR(100) NOT NULL,
    searched_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    INDEX idx_search_user (user_id)
);

-- Optional: Add Foreign Key Constraints (if users table exists)
-- ALTER TABLE follows ADD CONSTRAINT fk_follows_follower FOREIGN KEY (follower_id) REFERENCES users(id) ON DELETE CASCADE;
-- ALTER TABLE follows ADD CONSTRAINT fk_follows_following FOREIGN KEY (following_id) REFERENCES users(id) ON DELETE CASCADE;
-- ALTER TABLE notifications ADD CONSTRAINT fk_notifications_sender FOREIGN KEY (sender_id) REFERENCES users(id) ON DELETE CASCADE;
-- ALTER TABLE notifications ADD CONSTRAINT fk_notifications_receiver FOREIGN KEY (receiver_id) REFERENCES users(id) ON DELETE CASCADE;
-- ALTER TABLE search_history ADD CONSTRAINT fk_search_history_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE;

-- ============================================
-- INSERT SAMPLE DATA
-- ============================================

-- Insert sample data into follows table
INSERT INTO follows (follower_id, following_id, created_at) VALUES
(1, 2, '2026-05-10 08:15:00'),
(1, 3, '2026-05-11 10:30:00'),
(1, 4, '2026-05-12 14:45:00'),
(2, 1, '2026-05-10 09:00:00'),
(2, 5, '2026-05-13 11:20:00'),
(3, 1, '2026-05-09 16:50:00'),
(3, 2, '2026-05-14 13:15:00'),
(4, 1, '2026-05-15 07:30:00'),
(4, 3, '2026-05-15 09:45:00'),
(5, 2, '2026-05-16 15:20:00'),
(5, 4, '2026-05-17 12:10:00');

-- Insert sample data into notifications table
INSERT INTO notifications (sender_id, receiver_id, message, type, is_read, created_at) VALUES
(2, 1, 'john_doe started following you', 'FOLLOW', TRUE, '2026-05-10 08:20:00'),
(3, 1, 'jane_smith liked your post', 'LIKE', FALSE, '2026-05-11 10:35:00'),
(4, 1, 'mike_wilson started following you', 'FOLLOW', FALSE, '2026-05-12 14:50:00'),
(1, 2, 'You have a new follower: alex_jones', 'FOLLOW', TRUE, '2026-05-10 09:05:00'),
(5, 2, 'sarah_parker commented on your post', 'COMMENT', FALSE, '2026-05-13 11:25:00'),
(1, 3, 'jane_smith, you have a new message', 'MESSAGE', FALSE, '2026-05-14 13:20:00'),
(2, 4, 'Your post reached 100 likes!', 'MILESTONE', TRUE, '2026-05-15 07:35:00'),
(3, 5, 'mike_wilson started following you', 'FOLLOW', FALSE, '2026-05-16 15:25:00'),
(1, 5, 'You are trending now!', 'TRENDING', FALSE, '2026-05-17 12:15:00'),
(4, 2, 'New post from john_doe - Check it out', 'POST', FALSE, '2026-05-18 08:00:00');

-- Insert sample data into search_history table
INSERT INTO search_history (user_id, searched_user_id, searched_keyword, searched_at) VALUES
(1, 2, 'john_doe', '2026-05-10 08:10:00'),
(1, 3, 'jane_smith', '2026-05-11 10:25:00'),
(1, NULL, 'travel', '2026-05-12 14:40:00'),
(2, 1, 'alex_jones', '2026-05-10 08:55:00'),
(2, 5, 'sarah_parker', '2026-05-13 11:15:00'),
(3, 1, 'alex_jones', '2026-05-09 16:45:00'),
(3, NULL, 'photography', '2026-05-14 13:10:00'),
(4, 1, 'alex_jones', '2026-05-15 07:25:00'),
(4, 3, 'jane_smith', '2026-05-15 09:40:00'),
(5, 2, 'john_doe', '2026-05-16 15:15:00'),
(5, NULL, 'fitness', '2026-05-17 12:05:00'),
(1, 4, 'mike_wilson', '2026-05-18 08:30:00'),
(2, 3, 'jane_smith', '2026-05-18 09:15:00'),
(3, 5, 'sarah_parker', '2026-05-18 10:00:00');
