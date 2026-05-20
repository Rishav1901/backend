package com.insta.post.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.reactive.function.client.WebClient;

import com.insta.post.dto.HashtagDTO;
import com.insta.post.dto.PostResponseDTO;
import com.insta.post.exception.InstaPostManagementException;
import com.insta.post.service.InstaPostHashtagService;
import com.insta.post.service.InstaPostService;

@RestController
@RequestMapping("/api/trending")
public class InstaTrending {
	
	@Autowired
	private WebClient.Builder webClient;
	
	@Autowired
	private InstaPostService instaPostService;
	
	@Autowired
	private InstaPostHashtagService instaPostHashtagService;
	
	@GetMapping("/posts")
	public ResponseEntity<List<PostResponseDTO>> getTrendingPosts(
			@RequestHeader(value = "X-User-Id", required = false) Long loggedInUserId) throws InstaPostManagementException {
		List<Long> postIds = webClient
								.build()
								.get()
								.uri("http://TrendingPostsMS/api/trending/posts/ids")
								.retrieve()
								.bodyToFlux(Long.class)
								.collectList()
								.block();
		return new ResponseEntity<List<PostResponseDTO>>(instaPostService.getAllPostDetails(postIds, loggedInUserId), HttpStatus.OK);
	}
	
	@GetMapping("/hashtags")
	public ResponseEntity<List<HashtagDTO>> getTrendingHashtags() throws InstaPostManagementException{
		List<Long> hashtagIds = webClient
								.build()
								.get()
								.uri("http://TrendingPostsMS/api/trending/hashtags/ids")
								.retrieve()
								.bodyToFlux(Long.class)
								.collectList()
								.block();
		return new ResponseEntity<List<HashtagDTO>>(instaPostHashtagService.findByTagIdList(hashtagIds), HttpStatus.OK);
	}
}
