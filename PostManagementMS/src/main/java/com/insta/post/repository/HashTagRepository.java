package com.insta.post.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.insta.post.entity.Hashtag;

public interface HashTagRepository extends JpaRepository<Hashtag, Long>{
	Optional<Hashtag> findByTag(String tag);
	
	@Query("SELECT t FROM Hashtag t where t.tag LIKE :tag%")
	List<Hashtag> findByQuerytag(@Param("tag") String tag);
}
