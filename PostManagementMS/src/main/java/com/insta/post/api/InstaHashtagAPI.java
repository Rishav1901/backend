package com.insta.post.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestHeader;

import com.insta.post.dto.HashtagDTO;
import com.insta.post.dto.PostResponseDTO;
import com.insta.post.exception.InstaPostManagementException;
import com.insta.post.service.InstaPostHashtagService;
import com.insta.post.service.InstaPostService;

import jakarta.validation.constraints.NotBlank;

@RestController
@RequestMapping("/api/search")
@Validated
public class InstaHashtagAPI {
	@Autowired
	private InstaPostHashtagService instaPostHashtagService;
	
	@Autowired
	private InstaPostService instaPostService;

	
	@GetMapping("/hashtag")
	public ResponseEntity<List<HashtagDTO>> findHashtags(
			@RequestParam("tag") @NotBlank(message = "Please provide a valid tag") String tag)
			throws InstaPostManagementException{
		List<HashtagDTO> res = instaPostHashtagService.findByQuerytag(tag);
		return new ResponseEntity<List<HashtagDTO>>(res, HttpStatus.OK);
	}
	
	@GetMapping("/hashtag/{hashtagId}")
	public ResponseEntity<List<PostResponseDTO>> findPostsByHashtagId(
			@PathVariable("hashtagId") Long hashtagId,
			@RequestHeader(value = "X-User-Id", required = false) Long loggedInUserId) throws InstaPostManagementException {
		List<Long> postIdslist = instaPostHashtagService.findDistinctPostIdsByHashtagId(hashtagId);
		return new ResponseEntity<List<PostResponseDTO>>(instaPostService.getAllPostDetails(postIdslist, loggedInUserId), HttpStatus.OK);
	}

	@GetMapping("/hashtag/{hashtagId}/raw-ids")
	public ResponseEntity<List<Long>> findRawIdsByHashtagId(@PathVariable("hashtagId") Long hashtagId) throws InstaPostManagementException {
		return new ResponseEntity<>(instaPostHashtagService.findDistinctPostIdsByHashtagId(hashtagId), HttpStatus.OK);
	}
}
