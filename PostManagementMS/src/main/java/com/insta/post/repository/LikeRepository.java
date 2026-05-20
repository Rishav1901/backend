package com.insta.post.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.insta.post.entity.Like;

public interface LikeRepository extends JpaRepository<Like, Long> {

	List<Like> findByPost_PostId(Long postId);

	Optional<Like> findFirstByUserIdAndPost_PostId(Long userId, Long postId);

	boolean existsByUserIdAndPost_PostId(Long userId, Long postId);

	void deleteByUserIdAndPost_PostId(Long userId, Long postId);
	
	@Query("SELECT COUNT(l) FROM Like l WHERE l.post.postId = :postId")
    Long countLikesByPostId(@Param("postId") Long postId);

}
