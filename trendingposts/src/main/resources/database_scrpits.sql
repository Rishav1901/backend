CREATE DATABASE IF NOT EXISTS instagram_trending_db;

USE instagram_trending_db;

CREATE TABLE trending_posts (
	trending_post_id BIGINT AUTO_INCREMENT PRIMARY KEY,
	post_id BIGINT NOT NULL,
	trending_score DOUBLE NOT NULL,
	total_likes BIGINT DEFAULT 0,
	total_comments BIGINT DEFAULT 0,
	total_views BIGINT DEFAULT 0,
	calculated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	CONSTRAINT uk_trending_post	UNIQUE(post_id)
);

CREATE TABLE trending_hashtags (
	trending_hashtag_id BIGINT AUTO_INCREMENT PRIMARY KEY,
	hashtag_id BIGINT NOT NULL,
	trending_score DOUBLE NOT NULL,
	usage_count BIGINT DEFAULT 0,
	calculated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	CONSTRAINT uk_trending_hashtag	UNIQUE(hashtag_id)
);

CREATE INDEX idx_trending_posts_score
ON trending_posts(trending_score);

CREATE INDEX idx_trending_hashtags_score
ON trending_hashtags(trending_score);


