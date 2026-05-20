package com.insta.post.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.insta.post.entity.View;

public interface ViewRepository extends JpaRepository<View, Long> {

	List<View> findByPost_PostId(Long postId);
	
	@Query("SELECT COUNT(v) FROM View v WHERE v.post.postId = :postId")
    Long countViewsByPostId(@Param("postId") Long postId);

}
