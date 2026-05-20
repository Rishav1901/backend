package com.insta.post.entity;

import com.insta.post.dto.MediaType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table
@Data
public class PostMedia {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long mediaId;
	@Lob
	@Column(columnDefinition = "LONGBLOB")
	@jakarta.persistence.Basic(fetch = jakarta.persistence.FetchType.LAZY)
	private byte[] media;
	@Enumerated(EnumType.STRING)
	private MediaType mediaType;
	private Integer mediaOrder;
	@ManyToOne
	@JoinColumn(name = "post_id")
	private Post post;
}
