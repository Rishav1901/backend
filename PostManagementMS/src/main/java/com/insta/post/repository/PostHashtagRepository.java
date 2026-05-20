package com.insta.post.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.insta.post.entity.PostHashtag;

public interface PostHashtagRepository extends JpaRepository<PostHashtag, Long>{

	List<PostHashtag> findByPost_PostId(Long postId);

	@Query("SELECT DISTINCT ph.post.postId FROM PostHashtag ph WHERE ph.hashtag.hashtagId = :hashtagId")
    List<Long> findDistinctPostIdsByHashtagId(@Param("hashtagId") Long hashtagId);
	
}
