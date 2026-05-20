package com.insta.post.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.insta.post.entity.SharedPost;

@Repository
public interface SharedPostRepository extends JpaRepository<SharedPost, Long> {
	List<SharedPost> findByPost_PostId(Long postId);

	boolean existsByPost_PostIdAndUserId(Long postId, Long userId);
}
