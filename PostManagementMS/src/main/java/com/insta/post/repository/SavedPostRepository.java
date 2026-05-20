package com.insta.post.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

import com.insta.post.entity.SavedPost;

public interface SavedPostRepository extends JpaRepository<SavedPost, Long> {

	List<SavedPost> findByPost_PostId(Long postId);

	List<SavedPost> findByUserId(Long userId);

	Optional<SavedPost> findFirstByUserIdAndPost_PostId(Long userId, Long postId);

	@Query("SELECT COUNT(v) FROM SavedPost v WHERE v.post.postId = :postId")
    Long countSavesByPostId(@Param("postId") Long postId);
}
