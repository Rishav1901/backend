package com.insta.post.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "post_hashtags")
@Data
public class PostHashtag {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long postHashtagId;

	@ManyToOne
	@JoinColumn(name = "post_id")
	private Post post;

	@ManyToOne
	@JoinColumn(name = "hashtag_id")
	private Hashtag hashtag;
}
