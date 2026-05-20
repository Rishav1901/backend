DROP SCHEMA IF EXISTS  instagram_post_db;
CREATE DATABASE IF NOT EXISTS instagram_post_db;

USE instagram_post_db;

CREATE TABLE posts (

post_id BIGINT AUTO_INCREMENT PRIMARY KEY,

user_id BIGINT NOT NULL,

caption VARCHAR(2000),

media LONGBLOB,

media_type ENUM(
'IMAGE',
'VIDEO'
) NOT NULL,
 
visibility ENUM(
'PUBLIC',
'FOLLOWERS',
'PRIVATE'
) DEFAULT 'PUBLIC',

created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE post_media (
	media_id BIGINT AUTO_INCREMENT PRIMARY KEY,
	
	post_id BIGINT NOT NULL,
	
	media LONGBLOB NOT NULL,
	
	media_type ENUM(
	'IMAGE',
	'VIDEO'
	) NOT NULL,
 	
 	media_order INT DEFAULT 0,
 	
	CONSTRAINT fk_post_media_post
	FOREIGN KEY (post_id)
	REFERENCES posts(post_id)
	ON DELETE CASCADE
);

CREATE TABLE comments (

comment_id BIGINT AUTO_INCREMENT PRIMARY KEY,

post_id BIGINT NOT NULL,

user_id BIGINT NOT NULL,

parent_comment_id BIGINT NULL,

comment VARCHAR(1000) NOT NULL,

created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
ON UPDATE CURRENT_TIMESTAMP,

CONSTRAINT fk_comment_post
FOREIGN KEY (post_id)
REFERENCES posts(post_id)
ON DELETE CASCADE,

CONSTRAINT fk_parent_comment
FOREIGN KEY (parent_comment_id)
REFERENCES comments(comment_id)
ON DELETE CASCADE
);

CREATE TABLE likes (

like_id BIGINT AUTO_INCREMENT PRIMARY KEY,

post_id BIGINT NOT NULL,

user_id BIGINT NOT NULL,

created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

CONSTRAINT uk_post_like
UNIQUE(post_id, user_id),

CONSTRAINT fk_like_post
FOREIGN KEY (post_id)
REFERENCES posts(post_id)
ON DELETE CASCADE
);

CREATE TABLE saved_posts (

saved_post_id BIGINT AUTO_INCREMENT PRIMARY KEY,

post_id BIGINT NOT NULL,

user_id BIGINT NOT NULL,

created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

CONSTRAINT uk_saved_post
UNIQUE(post_id, user_id),

CONSTRAINT fk_saved_post
FOREIGN KEY (post_id)
REFERENCES posts(post_id)
ON DELETE CASCADE
);

CREATE TABLE hashtags (

hashtag_id BIGINT AUTO_INCREMENT PRIMARY KEY,

tag VARCHAR(100) NOT NULL UNIQUE,

usage_count BIGINT DEFAULT 0,

created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE post_hashtags (

post_hashtag_id BIGINT AUTO_INCREMENT PRIMARY KEY,

post_id BIGINT NOT NULL,

hashtag_id BIGINT NOT NULL,

CONSTRAINT uk_post_hashtag
UNIQUE(post_id, hashtag_id),

CONSTRAINT fk_post_hashtag_post
FOREIGN KEY (post_id)
REFERENCES posts(post_id)
ON DELETE CASCADE,

CONSTRAINT fk_post_hashtag_hashtag
FOREIGN KEY (hashtag_id)
REFERENCES hashtags(hashtag_id)
ON DELETE CASCADE
);

CREATE TABLE views (

post_view_id BIGINT AUTO_INCREMENT PRIMARY KEY,

post_id BIGINT NOT NULL,

user_id BIGINT NULL,

ip_address VARCHAR(100),

viewed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

CONSTRAINT uk_view
UNIQUE(post_id, user_id),

CONSTRAINT fk_post_view_post
FOREIGN KEY (post_id)
REFERENCES posts(post_id)
ON DELETE CASCADE
);

CREATE INDEX idx_posts_user
ON posts(user_id);

CREATE INDEX idx_posts_visibility
ON posts(visibility);

CREATE INDEX idx_posts_created_at
ON posts(created_at);

CREATE INDEX idx_comments_post
ON comments(post_id);

CREATE INDEX idx_comments_user
ON comments(user_id);

CREATE INDEX idx_likes_post
ON likes(post_id);

CREATE INDEX idx_likes_user
ON likes(user_id);

CREATE INDEX idx_saved_posts_user
ON saved_posts(user_id);

CREATE INDEX idx_hashtags_tag_name
ON hashtags(tag_name);

CREATE INDEX idx_views_post
ON views(post_id);

CREATE INDEX idx_views_user
ON views(user_id);