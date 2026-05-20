package com.insta.post.api;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;

import com.insta.post.dto.PostDTO;
import com.insta.post.dto.PostResponseDTO;
import com.insta.post.exception.InstaPostManagementException;
import com.insta.post.service.InstaPostCommentService;
import com.insta.post.service.InstaPostHashtagService;
import com.insta.post.service.InstaPostLikeService;
import com.insta.post.service.InstaPostSavedService;
import com.insta.post.service.InstaPostService;
import com.insta.post.service.InstaPostViewService;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@RestController
@RequestMapping("/api/users")
@Validated
public class InstaPostUsersAPI {
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
	@Autowired
	Environment environment;
	

	@DeleteMapping("/posts/{postId}")
	public ResponseEntity<String> deletePost(@RequestHeader("X-User-Id") @NotNull @Positive Long userId,
			@PathVariable Long postId) throws InstaPostManagementException {
		instaPostService.deletePost(userId, postId);
		String message = environment.getProperty("API.POST_DELETED") + postId;
		return new ResponseEntity<>(message, HttpStatus.OK);
	}

	@PostMapping("/{postId}/likes")
	public ResponseEntity<String> likePost(@RequestHeader("X-User-Id") @NotNull @Positive Long userId,
			@PathVariable Long postId) throws InstaPostManagementException {
		Long id = instaPostLikeService.likePost(userId, postId);
		String message = environment.getProperty("API.POST_LIKED") + id;
		return new ResponseEntity<>(message, HttpStatus.CREATED);
	}

	@DeleteMapping("/{postId}/likes")
	public ResponseEntity<String> unlikePost(@RequestHeader("X-User-Id") @NotNull @Positive Long userId,
			@PathVariable Long postId) throws InstaPostManagementException {
		Long id = instaPostLikeService.unlikePost(userId, postId);
		String message = environment.getProperty("API.POST_UNLIKED") + id;
		return new ResponseEntity<>(message, HttpStatus.OK);
	}

	@PostMapping("/{postId}/comment")
	public ResponseEntity<String> commentPost(@RequestHeader("X-User-Id") @NotNull @Positive Long userId,
			@PathVariable Long postId, @RequestBody String comment) throws InstaPostManagementException {
		Long id = instaPostCommentService.commentPost(userId, postId, comment);
		String message = environment.getProperty("API.POST_COMMENT") + id;
		return new ResponseEntity<>(message, HttpStatus.CREATED);
	}

	@PutMapping("/{postId}/comment/{commentId}")
	public ResponseEntity<String> editComment(@RequestHeader("X-User-Id") @NotNull @Positive Long userId,
			@PathVariable Long postId, @PathVariable Long commentId, @RequestBody String newComment) throws InstaPostManagementException {
		instaPostCommentService.editComment(userId, postId, commentId, newComment);
		return new ResponseEntity<>("Comment updated successfully", HttpStatus.OK);
	}

	@DeleteMapping("/{postId}/comment/{commentId}")
	public ResponseEntity<String> deleteComment(@RequestHeader("X-User-Id") @NotNull @Positive Long userId,
			@PathVariable Long postId, @PathVariable Long commentId) throws InstaPostManagementException {
		instaPostCommentService.deleteComment(userId, postId, commentId);
		return new ResponseEntity<>("Comment deleted successfully", HttpStatus.OK);
	}

//	@PostMapping("{postId}/posthashtag")
//	public ResponseEntity<String> postHashtag(@PathVariable Long postId, @RequestBody String tag)
//			throws InstaPostManagementException {
//		Long id = instaPostHashtagService.postHashtag(postId, tag);
//		String message = environment.getProperty("API.POST_HASHTAG") + id;
//		return new ResponseEntity<>(message, HttpStatus.CREATED);
//	}

	@PostMapping("/{postId}/savepost")
	public ResponseEntity<String> savePost(@RequestHeader("X-User-Id") @NotNull @Positive Long userId,
			@PathVariable Long postId) throws InstaPostManagementException {
		Long id = instaPostSavedService.createSavePost(userId, postId);
		String message = environment.getProperty("API.POST_SAVEPOST") + id;
		return new ResponseEntity<>(message, HttpStatus.CREATED);
	}

	@DeleteMapping("/{postId}/savepost")
	public ResponseEntity<String> unsavePost(@RequestHeader("X-User-Id") @NotNull @Positive Long userId,
			@PathVariable Long postId) throws InstaPostManagementException {
		Long id = instaPostSavedService.deleteSavePost(userId, postId);
		return new ResponseEntity<>("Post Unsaved: " + id, HttpStatus.OK);
	}

	@PostMapping("/{postId}/viewpost")
	public ResponseEntity<String> viewPost(@RequestHeader("X-User-Id") @NotNull @Positive Long userId,
			@PathVariable Long postId) throws InstaPostManagementException {
		Long id = instaPostViewService.viewPost(userId, postId);
		String message = environment.getProperty("API.POST_VIEW") + id;
		return new ResponseEntity<>(message, HttpStatus.CREATED);
	}

	@PostMapping("/{postId}/sharepost")
	public ResponseEntity<String> sharePost(@RequestHeader("X-User-Id") @NotNull @Positive Long userId,
			@PathVariable Long postId) throws InstaPostManagementException {
		instaPostService.sharePost(userId, postId);
		return new ResponseEntity<>("Post shared successfully", HttpStatus.CREATED);
	}

	@DeleteMapping("/{postId}/sharepost")
	public ResponseEntity<String> unsharePost(@RequestHeader("X-User-Id") @NotNull @Positive Long userId,
			@PathVariable Long postId) throws InstaPostManagementException {
		instaPostService.unsharePost(userId, postId);
		return new ResponseEntity<>("Post unshared successfully", HttpStatus.OK);
	}

	@GetMapping("/{userId}")
	public ResponseEntity<List<PostResponseDTO>> getPostsByUserId(@PathVariable @NotNull @Positive Long userId,
			@RequestHeader(value = "X-User-Id", required = false) Long loggedInUserId)
			throws InstaPostManagementException {
		List<PostDTO> l = instaPostService.findPostsByUserId(userId);
		List<PostResponseDTO> nl = new ArrayList<>();
		
		boolean isSelf = loggedInUserId != null && loggedInUserId.equals(userId);
		boolean isFollower = false;
		if (loggedInUserId != null && !isSelf) {
			isFollower = instaPostService.isFollowingUser(loggedInUserId, userId);
		}

		for (PostDTO item : l) {
			boolean canView = false;
			if (isSelf) {
				canView = true;
			} else if (item.getVisibility() == com.insta.post.dto.Visibility.PUBLIC) {
				canView = true;
			} else if (item.getVisibility() == com.insta.post.dto.Visibility.FOLLOWERS && isFollower) {
				canView = true;
			}

			if (!canView) {
				continue;
			}

			PostResponseDTO p = new PostResponseDTO();
			p.setPostId(item.getPostId());
			p.setAuthor(instaPostService.getUserDTO(item.getUserId()));
			p.setCaption(item.getCaption());
			p.setMediaList(item.getMediaList());
			try {
				p.setHashtags(instaPostHashtagService.getPostHashtag(item.getPostId()));
				p.setTotalLikes(instaPostLikeService.countLikesByPostId(item.getPostId()));
				p.setTotalComments(instaPostCommentService.countCommentsByPostId(item.getPostId()));
				p.setTotalViews(instaPostViewService.countViewsByPostId(item.getPostId()));
				p.setTotalSaves(instaPostSavedService.countSavesByPostId(item.getPostId()));
				p.setLikedByUser(instaPostLikeService.isPostLikedByUser(loggedInUserId, item.getPostId()));
				p.setSavedByUser(instaPostSavedService.isPostSavedByUser(loggedInUserId, item.getPostId()));
			} catch (InstaPostManagementException e) {
				e.printStackTrace();
			}
			p.setCreatedAt(item.getCreatedAt());
			nl.add(p);
		}
		return new ResponseEntity<>(nl, HttpStatus.OK);
	}
	
	
}
