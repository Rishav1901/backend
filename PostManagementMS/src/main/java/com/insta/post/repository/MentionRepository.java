package com.insta.post.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.insta.post.entity.Mention;

@Repository
public interface MentionRepository extends JpaRepository<Mention, Long> {
	List<Mention> findByMentionedUserId(Long userId);

	List<Mention> findByPost_PostId(Long postId);

	List<Mention> findByComment_CommentId(Long commentId);
}
