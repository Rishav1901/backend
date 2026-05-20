
package com.insta.post.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.insta.post.dto.CommentDTO;
import com.insta.post.dto.HashtagDTO;
import com.insta.post.dto.LikeDTO;
import com.insta.post.dto.PostDTO;
import com.insta.post.dto.PostResponseDTO;
import com.insta.post.dto.SavePostDTO;
import com.insta.post.dto.ViewDTO;
import com.insta.post.exception.InstaPostManagementException;
import com.insta.post.service.InstaPostCommentService;
import com.insta.post.service.InstaPostHashtagService;
import com.insta.post.service.InstaPostLikeService;
import com.insta.post.service.InstaPostSavedService;
import com.insta.post.service.InstaPostService;
import com.insta.post.service.InstaPostViewService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@RestController
@RequestMapping("/api/posts")
@Validated
public class InstaPostAPI {
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

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<String> createPost(@RequestHeader("X-User-Id") @NotNull @Positive Long userId,
			@Valid @RequestPart("post") PostDTO dto,
			@RequestPart("files") MultipartFile[] files) throws InstaPostManagementException {

		dto.setUserId(userId);
		Long id = instaPostService.createPost(dto, files);
		String message = environment.getProperty("API.POST_CREATED") + id;
		return new ResponseEntity<>(message, HttpStatus.CREATED);
	}

	@GetMapping("/{postId:[0-9]+}")
	public ResponseEntity<PostResponseDTO> getPostById(@PathVariable Long postId,
			@RequestHeader(value = "X-User-Id", required = false) Long userId) throws InstaPostManagementException {
		PostResponseDTO p = instaPostService.getAllPostDetailsById(postId, userId);
		return new ResponseEntity<>(p, HttpStatus.OK);
	}

	@GetMapping("/{postId}/likes")
	public ResponseEntity<List<LikeDTO>> getAllLikeByPostId(@PathVariable Long postId)
			throws InstaPostManagementException {
		List<LikeDTO> like = instaPostLikeService.getAllLikeByPostId(postId);
		return new ResponseEntity<>(like, HttpStatus.OK);
	}

	@GetMapping("{postId}/comment")
	public ResponseEntity<List<CommentDTO>> getCommentPost(@PathVariable Long postId)
			throws InstaPostManagementException {
		List<CommentDTO> c = instaPostCommentService.getAllCommentPostId(postId);
		return new ResponseEntity<>(c, HttpStatus.OK);
	}

	@GetMapping("/{postId}/posthashtag")
	public ResponseEntity<List<String>> getPostHashtag(@PathVariable Long postId)
			throws InstaPostManagementException {
		List<String> t = instaPostHashtagService.getPostHashtag(postId);
		return new ResponseEntity<>(t, HttpStatus.OK);
	}

	@GetMapping("{postId}/savepost")
	public ResponseEntity<List<SavePostDTO>> getAllSavePostByPostId(@PathVariable Long postId)
			throws InstaPostManagementException {
		List<SavePostDTO> s = instaPostSavedService.getAllSavePost(postId);
		return new ResponseEntity<>(s, HttpStatus.OK);
	}

	@GetMapping("/{postId}/viewpost")
	public ResponseEntity<List<ViewDTO>> getAllViewById(@PathVariable Long postId) throws InstaPostManagementException {
		List<ViewDTO> v = instaPostViewService.getAllViewById(postId);
		return new ResponseEntity<>(v, HttpStatus.OK);
	}

	@GetMapping("/tags")
	public ResponseEntity<HashtagDTO> getHashtag(
			@RequestParam("tag") @NotBlank(message = "Please provide a valid tag") String tag)
			throws InstaPostManagementException {
		HashtagDTO t = instaPostHashtagService.findHashtagByTag(tag);
		return new ResponseEntity<>(t, HttpStatus.OK);
	}

	@GetMapping("/feed")
	public ResponseEntity<List<PostResponseDTO>> getPosts(
			@RequestHeader(value = "X-User-Id", required = false) Long userId) throws InstaPostManagementException {
		return new ResponseEntity<>(instaPostService.getFeedPosts(userId), HttpStatus.OK);
	}

	@GetMapping("/details")
	public ResponseEntity<List<PostResponseDTO>> getPostsDetailsByIds(
			@RequestParam("ids") List<Long> postIds,
			@RequestHeader(value = "X-User-Id", required = false) Long userId) throws InstaPostManagementException {
		return new ResponseEntity<>(instaPostService.getAllPostDetails(postIds, userId), HttpStatus.OK);
	}

	@GetMapping("/saved")
	public ResponseEntity<List<PostResponseDTO>> getSavedPosts(
			@RequestHeader("X-User-Id") @NotNull @Positive Long userId) throws InstaPostManagementException {
		return new ResponseEntity<>(instaPostService.getSavedPosts(userId), HttpStatus.OK);
	}

	@PutMapping(value = "/{postId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<String> editPost(@RequestHeader("X-User-Id") @NotNull @Positive Long userId,
			@PathVariable Long postId,
			@Valid @RequestPart("post") PostDTO dto,
			@RequestPart(value = "files", required = false) MultipartFile[] files) throws InstaPostManagementException {
		instaPostService.editPost(userId, postId, dto, files);
		return new ResponseEntity<>("Post updated successfully", HttpStatus.OK);
	}

	@org.springframework.web.bind.annotation.PatchMapping(value = "/{postId}", consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> editPostJson(@RequestHeader("X-User-Id") @NotNull @Positive Long userId,
			@PathVariable Long postId,
			@RequestBody PostDTO dto) throws InstaPostManagementException {
		instaPostService.editPost(userId, postId, dto, null);
		return new ResponseEntity<>("Post updated successfully", HttpStatus.OK);
	}

	@GetMapping("/media/{mediaId}")
	public ResponseEntity<byte[]> getMedia(@PathVariable Long mediaId) throws InstaPostManagementException {
		com.insta.post.entity.PostMedia media = instaPostService.getPostMedia(mediaId);
		org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
		if (media.getMediaType() == com.insta.post.dto.MediaType.VIDEO) {
			headers.setContentType(MediaType.parseMediaType("video/mp4"));
		} else {
			headers.setContentType(MediaType.IMAGE_JPEG);
		}
		headers.setCacheControl("max-age=31536000"); // Cache for 1 year
		return new ResponseEntity<>(media.getMedia(), headers, HttpStatus.OK);
	}
}
