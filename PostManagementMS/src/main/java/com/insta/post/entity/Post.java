package com.insta.post.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.insta.post.dto.Visibility;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "posts")
@Data
public class Post {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long postId;
	private Long userId;
	private String caption;
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = jakarta.persistence.FetchType.EAGER)
	private List<PostMedia> mediaList = new ArrayList<>();
	private Visibility visibility;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL)
	private List<Comment> comments;
	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL)
	private List<View> views;
	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL)
	private List<PostHashtag> postHashTags;
	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL)
	private List<SavedPost> savedPosts;
	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL)
	private List<Like> likes;
}
