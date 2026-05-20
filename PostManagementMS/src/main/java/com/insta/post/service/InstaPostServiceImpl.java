package com.insta.post.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.time.LocalDateTime;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import com.insta.post.dto.MediaType;
import com.insta.post.dto.PostDTO;
import com.insta.post.dto.PostResponseDTO;
import com.insta.post.dto.UserDTO;
import com.insta.post.entity.Post;
import com.insta.post.entity.PostMedia;
import com.insta.post.entity.SharedPost;
import com.insta.post.exception.InstaPostManagementException;
import com.insta.post.repository.PostManagementRepository;
import com.insta.post.repository.SharedPostRepository;
import com.insta.post.repository.CommentRepository;
import com.insta.post.repository.LikeRepository;
import com.insta.post.repository.SavedPostRepository;
import com.insta.post.repository.ViewRepository;
import com.insta.post.repository.PostHashtagRepository;
import com.insta.post.repository.PostMediaRepository;

import com.insta.post.entity.Comment;
import com.insta.post.entity.Like;
import com.insta.post.entity.SavedPost;
import com.insta.post.entity.View;
import com.insta.post.entity.PostHashtag;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.PageRequest;

@Service("instaPostService")
public class InstaPostServiceImpl implements InstaPostService {
	@Autowired
	PostManagementRepository postManagementRepository;
	@Autowired
	InstaPostCommentService instaPostCommentService;
	@Autowired
	InstaPostLikeService instaPostLikeService;
	
	@Autowired
	InstaPostViewService instaPostViewService;
	
	@Autowired
	InstaPostSavedService instaPostSavedService;
	
	@Autowired
	ModelMapper modelMapper;
	@Autowired
	InstaPostHashtagService instaPostHashtagService;
	
	@Autowired
	WebClient.Builder webClientBuilder;

	@Autowired
	SharedPostRepository sharedPostRepository;

	@Autowired
	CommentRepository commentRepository;

	@Autowired
	LikeRepository likeRepository;

	@Autowired
	SavedPostRepository savedPostRepository;

	@Autowired
	ViewRepository viewRepository;

	@Autowired
	PostHashtagRepository postHashtagRepository;

	@Autowired
	PostMediaRepository postMediaRepository;

	private final ConcurrentHashMap<Long, UserDTO> userCache = new ConcurrentHashMap<>();
	private final ExecutorService executorService = Executors.newFixedThreadPool(15);

	@Override
	@Transactional
	public PostDTO findPostById(Long postId) throws InstaPostManagementException {
		Optional<Post> optional = postManagementRepository.findById(postId);
		Post p = optional.orElseThrow(() -> new InstaPostManagementException("SERVICE.POST_NOT_FOUND"));
		return modelMapper.map(p, PostDTO.class);
	}

	@Override
	public Long createPost(PostDTO dto, MultipartFile[] files) throws InstaPostManagementException {
		if (files == null || files.length == 0) {
			throw new InstaPostManagementException("SERVICE.MEDIA_REQUIRED");
		}

		List<String> tags = new ArrayList<>();
		String text = dto.getCaption();
		Pattern pattern = Pattern.compile("#\\w+");
		Matcher matcher = pattern.matcher(text);
		while (matcher.find()) {
			tags.add(matcher.group());
		}
		dto.setCaption(text.replaceAll("#\\w+", "").replaceAll("\\s+", " ").trim());
		Post post = modelMapper.map(dto, Post.class);
		post.setCreatedAt(LocalDateTime.now());
		post.setUpdatedAt(LocalDateTime.now());
		int order = 0;
		for (MultipartFile file : files) {
			String contentType = file.getContentType();
			if (contentType == null || !(contentType.startsWith("image/") || contentType.startsWith("video/"))) {
				throw new InstaPostManagementException("SERVICE.INVALID_MEDIA_TYPE");
			}
			try {
				PostMedia media = new PostMedia();
				media.setMedia(file.getBytes());
				if (contentType.startsWith("image/")) {
					media.setMediaType(MediaType.IMAGE);
				}
				if (contentType.startsWith("video/")) {
					media.setMediaType(MediaType.VIDEO);
				}
				media.setMediaOrder(order++);
				media.setPost(post);
				post.getMediaList().add(media);
			} catch (IOException exception) {
				throw new InstaPostManagementException("SERVICE.MEDIA_UPLOAD_FAILED");
			}
		}
		Post saved = postManagementRepository.save(post);
		for (String tag : tags) {
			instaPostHashtagService.postHashtag(saved.getPostId(), tag);
		}
		return saved.getPostId();
	}

	@Override
	public List<PostDTO> findPostsByUserId(Long userId) throws InstaPostManagementException {
		List<Post> posts = postManagementRepository.findByUserIdOrderByCreatedAtDesc(userId);
		if (posts.isEmpty())
			throw new InstaPostManagementException("SERVICE.POST_NOT_FOUND");

		List<PostDTO> result = new ArrayList<>();
		posts.forEach(post -> result.add(modelMapper.map(post, PostDTO.class)));

		return result;
	}

	@Override
	@Transactional
	public void deletePost(Long userId, Long postId) throws InstaPostManagementException {
		Post post = postManagementRepository.findByUserIdAndPostId(userId, postId)
				.orElseThrow(() -> new InstaPostManagementException("SERVICE.POST_NOT_FOUND"));
		
		likeRepository.deleteAll(likeRepository.findByPost_PostId(postId));
		commentRepository.deleteAll(commentRepository.findByPost_PostId(postId));
		savedPostRepository.deleteAll(savedPostRepository.findByPost_PostId(postId));
		viewRepository.deleteAll(viewRepository.findByPost_PostId(postId));
		postHashtagRepository.deleteAll(postHashtagRepository.findByPost_PostId(postId));
		sharedPostRepository.deleteAll(sharedPostRepository.findByPost_PostId(postId));
		
		postManagementRepository.delete(post);
	}

	@Override
	public List<Object[]> findAllPostsWithoutData() throws InstaPostManagementException {
		return postManagementRepository.findAllPostsWithoutData();
	}

	@Override
	public List<PostDTO> getAllPosts() throws InstaPostManagementException {
		List<Post> posts = postManagementRepository.findAll();
		if (posts.isEmpty()) {
			throw new InstaPostManagementException("SERVICE.POSTS_NOT_FOUND");
		}
		List<PostDTO> result = new ArrayList<>();
		posts.forEach(post -> result.add(modelMapper.map(post, PostDTO.class)));
		return result;
	}

	private PostResponseDTO populatePostDetails(PostDTO post, Long currentUserId) {
		PostResponseDTO item = new PostResponseDTO();
		item.setPostId(post.getPostId());
		item.setCreatedAt(post.getCreatedAt());
		item.setCaption(post.getCaption());
		item.setMediaList(post.getMediaList());

		item.setAuthor(getUserDTO(post.getUserId()));
		
		try {
			item.setHashtags(instaPostHashtagService.getPostHashtag(post.getPostId()));
		} catch (Exception e) {
			item.setHashtags(new ArrayList<>());
		}

		try {
			item.setTotalComments(instaPostCommentService.countCommentsByPostId(post.getPostId()));
		} catch (Exception e) {
			item.setTotalComments(0L);
		}

		try {
			item.setTotalLikes(instaPostLikeService.countLikesByPostId(post.getPostId()));
		} catch (Exception e) {
			item.setTotalLikes(0L);
		}

		try {
			item.setTotalViews(instaPostViewService.countViewsByPostId(post.getPostId()));
		} catch (Exception e) {
			item.setTotalViews(0L);
		}

		try {
			item.setTotalSaves(instaPostSavedService.countSavesByPostId(post.getPostId()));
		} catch (Exception e) {
			item.setTotalSaves(0L);
		}

		item.setLikedByUser(instaPostLikeService.isPostLikedByUser(currentUserId, post.getPostId()));
		item.setSavedByUser(instaPostSavedService.isPostSavedByUser(currentUserId, post.getPostId()));

		return item;
	}

	@Override
	public List<PostResponseDTO> getFeedPosts(Long userId) throws InstaPostManagementException {
		List<Post> feedPosts = new ArrayList<>();

		if (userId == null) {
			feedPosts = postManagementRepository.findAllByOrderByCreatedAtDesc(PageRequest.of(0, 20));
		} else {
			List<Long> feedUserIds = new ArrayList<>();
			feedUserIds.add(userId);
			try {
				List<Long> followingIds = webClientBuilder.build().get()
						.uri("http://FollowMS/api/follows/following/{userId}", userId)
						.retrieve()
						.bodyToFlux(Long.class)
						.collectList()
						.block();
				if (followingIds != null) {
					feedUserIds.addAll(followingIds);
				}
			} catch (Exception exception) {
				feedUserIds = List.of(userId);
			}

			feedPosts = postManagementRepository.findByUserIdInOrderByCreatedAtDesc(
					feedUserIds.stream().distinct().toList(),
					PageRequest.of(0, 20)
			);
		}

		if (feedPosts.isEmpty()) {
			feedPosts = postManagementRepository.findAllByOrderByCreatedAtDesc(PageRequest.of(0, 20));
		}

		final Long currentUserId = userId;
		List<CompletableFuture<PostResponseDTO>> futures = feedPosts.stream()
				.map(post -> modelMapper.map(post, PostDTO.class))
				.map(postDto -> CompletableFuture.supplyAsync(() -> {
					try {
						checkPostVisibility(postDto, currentUserId);
						return populatePostDetails(postDto, currentUserId);
					} catch (Exception e) {
						return null;
					}
				}, executorService))
				.toList();

		List<PostResponseDTO> result = new ArrayList<>();
		for (CompletableFuture<PostResponseDTO> future : futures) {
			PostResponseDTO dto = future.join();
			if (dto != null) {
				result.add(dto);
			}
		}

		return result;
	}

	@Override
	public List<PostResponseDTO> getAllPostDetails(List<Long> postIds, Long loggedInUserId) throws InstaPostManagementException {
		List<CompletableFuture<PostResponseDTO>> futures = postIds.stream()
				.map(postId -> CompletableFuture.supplyAsync(() -> {
					try {
						return getAllPostDetailsById(postId, loggedInUserId);
					} catch (Exception e) {
						e.printStackTrace();
						return null;
					}
				}, executorService))
				.toList();

		List<PostResponseDTO> result = new ArrayList<>();
		for (CompletableFuture<PostResponseDTO> future : futures) {
			try {
				PostResponseDTO dto = future.join();
				if (dto != null) {
					result.add(dto);
				}
			} catch (Exception e) {
				// Ignore
			}
		}
		return result;
	}
	
	@Override
	@Transactional
	public PostResponseDTO getAllPostDetailsById(Long postId, Long currentUserId) throws InstaPostManagementException{
		PostDTO post = findPostById(postId);
		checkPostVisibility(post, currentUserId);
		return populatePostDetails(post, currentUserId);
	}

	@Override
	public boolean isFollowingUser(Long currentUserId, Long targetUserId) {
		if (currentUserId == null || targetUserId == null) {
			return false;
		}
		if (currentUserId.equals(targetUserId)) {
			return true;
		}
		try {
			List<Long> followingIds = webClientBuilder.build().get()
					.uri("http://FollowMS/api/follows/following/{userId}", currentUserId)
					.retrieve()
					.bodyToFlux(Long.class)
					.collectList()
					.block();
			return followingIds != null && followingIds.contains(targetUserId);
		} catch (Exception exception) {
			return false;
		}
	}

	private void checkPostVisibility(PostDTO post, Long currentUserId) throws InstaPostManagementException {
		if (post.getVisibility() == com.insta.post.dto.Visibility.PUBLIC) {
			return; // Anyone can view
		}
		if (currentUserId == null) {
			throw new InstaPostManagementException("SERVICE.UNAUTHORIZED_POST_VIEW");
		}
		if (post.getUserId().equals(currentUserId)) {
			return; // Owner can view
		}
		if (post.getVisibility() == com.insta.post.dto.Visibility.PRIVATE) {
			throw new InstaPostManagementException("SERVICE.UNAUTHORIZED_POST_VIEW");
		}
		if (post.getVisibility() == com.insta.post.dto.Visibility.FOLLOWERS) {
			if (!isFollowingUser(currentUserId, post.getUserId())) {
				throw new InstaPostManagementException("SERVICE.UNAUTHORIZED_POST_VIEW");
			}
		}
	}

	@Override
	@Transactional
	public void editPost(Long userId, Long postId, PostDTO dto, MultipartFile[] files) throws InstaPostManagementException {
		Post post = postManagementRepository.findById(postId)
				.orElseThrow(() -> new InstaPostManagementException("SERVICE.POST_NOT_FOUND"));

		if (!post.getUserId().equals(userId)) {
			throw new InstaPostManagementException("SERVICE.UNAUTHORIZED_POST_EDIT");
		}

		List<String> tags = new ArrayList<>();
		String text = dto.getCaption();
		if (text != null) {
			Pattern pattern = Pattern.compile("#\\w+");
			Matcher matcher = pattern.matcher(text);
			while (matcher.find()) {
				tags.add(matcher.group());
			}
			post.setCaption(text.replaceAll("#\\w+", "").replaceAll("\\s+", " ").trim());
		}

		post.setVisibility(dto.getVisibility());
		post.setUpdatedAt(LocalDateTime.now());

		// If new files are uploaded, replace the existing ones
		if (files != null && files.length > 0) {
			post.getMediaList().clear();
			int order = 0;
			for (MultipartFile file : files) {
				String contentType = file.getContentType();
				if (contentType == null || !(contentType.startsWith("image/") || contentType.startsWith("video/"))) {
					throw new InstaPostManagementException("SERVICE.INVALID_MEDIA_TYPE");
				}
				try {
					PostMedia media = new PostMedia();
					media.setMedia(file.getBytes());
					if (contentType.startsWith("image/")) {
						media.setMediaType(MediaType.IMAGE);
					}
					if (contentType.startsWith("video/")) {
						media.setMediaType(MediaType.VIDEO);
					}
					media.setMediaOrder(order++);
					media.setPost(post);
					post.getMediaList().add(media);
				} catch (IOException exception) {
					throw new InstaPostManagementException("SERVICE.MEDIA_UPLOAD_FAILED");
				}
			}
		}

		postManagementRepository.save(post);

		instaPostHashtagService.deletePostHashtags(postId);
		for (String tag : tags) {
			instaPostHashtagService.postHashtag(postId, tag);
		}
	}

	@Override
	public void sharePost(Long userId, Long postId) throws InstaPostManagementException {
		Post post = postManagementRepository.findById(postId)
				.orElseThrow(() -> new InstaPostManagementException("SERVICE.POST_NOT_FOUND"));

		boolean alreadyShared = sharedPostRepository.existsByPost_PostIdAndUserId(postId, userId);
		if (alreadyShared) {
			throw new InstaPostManagementException("SERVICE.POST_ALREADY_SHARED");
		}

		SharedPost sharedPost = new SharedPost();
		sharedPost.setPost(post);
		sharedPost.setUserId(userId);
		sharedPost.setSharedAt(LocalDateTime.now());
		sharedPostRepository.save(sharedPost);
	}

	@Override
	public void unsharePost(Long userId, Long postId) throws InstaPostManagementException {
		Post post = postManagementRepository.findById(postId)
				.orElseThrow(() -> new InstaPostManagementException("SERVICE.POST_NOT_FOUND"));

		List<SharedPost> sharedPosts = sharedPostRepository.findByPost_PostId(postId);
		sharedPostRepository.deleteAll(sharedPosts.stream()
				.filter(sp -> sp.getUserId().equals(userId))
				.toList());
	}

	@Override
	public UserDTO getUserDTO(Long userId) {
		UserDTO cached = userCache.get(userId);
		if (cached != null) {
			return cached;
		}
		try {
			UserDTO fetched = webClientBuilder
					.build()
					.get()
					.uri("http://AuthenticationMS/api/users/userId/{userId}", userId)
					.retrieve()
					.bodyToMono(UserDTO.class)
					.block();
			if (fetched != null) {
				userCache.put(userId, fetched);
				return fetched;
			}
		} catch (Exception e) {
			System.err.println("Error fetching user DTO for id " + userId + ": " + e.getMessage());
			e.printStackTrace();
		}
		UserDTO fallback = new UserDTO();
		fallback.setUserId(userId);
		fallback.setUsername("user_" + userId);
		fallback.setFullName("User " + userId);
		return fallback;
	}

	@Override
	public List<PostResponseDTO> getSavedPosts(Long userId) throws InstaPostManagementException {
		List<SavedPost> savedList = savedPostRepository.findByUserId(userId);
		List<PostDTO> postDTOs = new ArrayList<>();
		for (SavedPost sp : savedList) {
			if (sp.getPost() != null) {
				postDTOs.add(modelMapper.map(sp.getPost(), PostDTO.class));
			}
		}

		postDTOs.sort(Comparator.comparing(PostDTO::getCreatedAt, Comparator.nullsLast(Comparator.naturalOrder())).reversed());

		List<CompletableFuture<PostResponseDTO>> futures = postDTOs.stream()
				.map(post -> CompletableFuture.supplyAsync(() -> {
					try {
						checkPostVisibility(post, userId);
						return populatePostDetails(post, userId);
					} catch (Exception e) {
						return null;
					}
				}, executorService))
				.toList();

		List<PostResponseDTO> result = new ArrayList<>();
		for (CompletableFuture<PostResponseDTO> future : futures) {
			PostResponseDTO dto = future.join();
			if (dto != null) {
				result.add(dto);
			}
		}

		return result;
	}

	@Override
	public PostMedia getPostMedia(Long mediaId) throws InstaPostManagementException {
		return postMediaRepository.findById(mediaId)
				.orElseThrow(() -> new InstaPostManagementException("SERVICE.MEDIA_NOT_FOUND"));
	}
}
