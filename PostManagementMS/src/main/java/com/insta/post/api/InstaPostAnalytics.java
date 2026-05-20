package com.insta.post.api;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.insta.post.dto.HashtagDTO;
import com.insta.post.dto.PostAnalyticsDTO;
import com.insta.post.exception.InstaPostManagementException;
import com.insta.post.service.InstaPostCommentService;
import com.insta.post.service.InstaPostHashtagService;
import com.insta.post.service.InstaPostLikeService;
import com.insta.post.service.InstaPostSavedService;
import com.insta.post.service.InstaPostService;
import com.insta.post.service.InstaPostViewService;

@RestController
@RequestMapping("/api/analytics")
public class InstaPostAnalytics {
	@Autowired
	InstaPostService instaPostService;
	@Autowired
	InstaPostCommentService instaPostCommentService;
	@Autowired
	InstaPostHashtagService instaPostHashtagService;
	@Autowired
	InstaPostLikeService instaPostLikeService;
	@Autowired
	InstaPostSavedService instaPostSavedService;
	@Autowired
	InstaPostViewService instaPostViewService;
	
	@GetMapping("/posts")
	public ResponseEntity<List<PostAnalyticsDTO>> getPostAnalytics() throws InstaPostManagementException{
		List<PostAnalyticsDTO> response = new ArrayList<>();
		List<Object[]> allPosts = instaPostService.findAllPostsWithoutData();
		for( Object[] postInfo : allPosts) {
			PostAnalyticsDTO item = new PostAnalyticsDTO();
			Long postId = (Long)postInfo[0];
			item.setPostId(postId);
			item.setPostCreatedAt((LocalDateTime) postInfo[1]);
			item.setTotalComments(instaPostCommentService.countCommentsByPostId(postId));
			item.setTotalLikes(instaPostLikeService.countLikesByPostId(postId));
			item.setTotalViews(instaPostViewService.countViewsByPostId(postId));
			item.setTotalSaves(instaPostSavedService.countSavesByPostId(postId));
			item.setCreatedAt(LocalDateTime.now());
			
			response.add(item);
		}
		return new ResponseEntity<List<PostAnalyticsDTO>>(response, HttpStatus.OK);
	}
	
	@GetMapping("/hashtags")
	public ResponseEntity<List<HashtagDTO>> getHashtagAnalytics() throws InstaPostManagementException{
		 List<HashtagDTO> hashtagList = instaPostHashtagService.findAllHashtags();
	     return new ResponseEntity<>(hashtagList, HttpStatus.OK);
	}
}
