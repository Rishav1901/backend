package com.insta.post.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.insta.post.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long>{

	List<Comment> findByPost_PostId(Long postId);

	@Query("SELECT COUNT(c) FROM Comment c WHERE c.post.postId = :postId")
    Long countCommentsByPostId(@Param("postId") Long postId);
}
