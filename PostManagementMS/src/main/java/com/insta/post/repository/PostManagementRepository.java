package com.insta.post.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.insta.post.entity.Post;

import jakarta.transaction.Transactional;

public interface PostManagementRepository extends JpaRepository<Post, Long>{
	List<Post> findByUserId(Long userId);
	Optional<Post> findByUserIdAndPostId(Long userId, Long postId);
	@Transactional
	void deleteByUserIdAndPostId(Long userId, Long postId);
	
	@Query("SELECT P.postId, P.createdAt from Post P where P.visibility=PUBLIC")
	List<Object[]> findAllPostsWithoutData();

	List<Post> findByUserIdOrderByCreatedAtDesc(Long userId);
	List<Post> findByUserIdInOrderByCreatedAtDesc(List<Long> userIds, org.springframework.data.domain.Pageable pageable);
	List<Post> findAllByOrderByCreatedAtDesc(org.springframework.data.domain.Pageable pageable);
}
