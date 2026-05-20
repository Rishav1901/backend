package com.insta.post.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.insta.post.dto.CommentDTO;
import com.insta.post.dto.UserDTO;
import com.insta.post.entity.Comment;
import com.insta.post.entity.Post;
import com.insta.post.exception.InstaPostManagementException;
import com.insta.post.repository.CommentRepository;
import com.insta.post.repository.PostManagementRepository;

@Service("instaPostCommentService")
public class InstaPostCommentServiceImpl implements InstaPostCommentService {
	@Autowired
	PostManagementRepository postManagementRepository;
	@Autowired
	ModelMapper modelMapper;
	@Autowired
	CommentRepository commentRepository;

	@Autowired
	WebClient.Builder webClientBuilder;

	private final ConcurrentHashMap<Long, String> usernameCache = new ConcurrentHashMap<>();

	private String getUsername(Long userId) {
		return usernameCache.computeIfAbsent(userId, id -> {
			try {
				UserDTO user = webClientBuilder
						.build()
						.get()
						.uri("http://AuthenticationMS/api/users/userId/{userId}", id)
						.retrieve()
						.bodyToMono(UserDTO.class)
						.block();
				return user != null ? user.getUsername() : "user_" + id;
			} catch (Exception e) {
				return "user_" + id;
			}
		});
	}

	@Override
	public Long commentPost(Long userId, Long postId, String comment) throws InstaPostManagementException {
		Comment c = new Comment();
		Post post = postManagementRepository.findById(postId)
				.orElseThrow(() -> new InstaPostManagementException("SERVICE.POST_NOT_FOUND"));
		c.setPost(post);
		c.setUserId(userId);
		c.setComment(comment);
		c.setCreatedAt(LocalDateTime.now());
		return commentRepository.save(c).getCommentId();
	}
	
	@Override
	public List<CommentDTO> getAllCommentPostId(Long postId) throws InstaPostManagementException {
		List<Comment> l = commentRepository.findByPost_PostId(postId);
		List<CommentDTO> nl = new ArrayList<>();
		if (l.isEmpty())
			return nl;
		l.forEach(c -> {
			CommentDTO n = new CommentDTO();
			n.setComment(c.getComment());
			n.setCommentId(c.getCommentId());
			n.setPostId(c.getPost().getPostId());
			n.setUserId(c.getUserId());
			n.setUsername(getUsername(c.getUserId()));
			n.setCreatedAt(c.getCreatedAt());
			nl.add(n);
		});
		return nl;
	}
	
	@Override
	public Long countCommentsByPostId(Long postId) throws InstaPostManagementException{
		return commentRepository.countCommentsByPostId(postId);
	}

	@Override
	public void editComment(Long userId, Long postId, Long commentId, String newComment) throws InstaPostManagementException {
		Comment comment = commentRepository.findById(commentId)
				.orElseThrow(() -> new InstaPostManagementException("SERVICE.COMMENT_NOT_FOUND"));

		if (!comment.getUserId().equals(userId)) {
			throw new InstaPostManagementException("SERVICE.UNAUTHORIZED_COMMENT_EDIT");
		}

		if (!comment.getPost().getPostId().equals(postId)) {
			throw new InstaPostManagementException("SERVICE.COMMENT_NOT_ON_POST");
		}

		comment.setComment(newComment);
		comment.setUpdatedAt(LocalDateTime.now());
		commentRepository.save(comment);
	}

	@Override
	public void deleteComment(Long userId, Long postId, Long commentId) throws InstaPostManagementException {
		Comment comment = commentRepository.findById(commentId)
				.orElseThrow(() -> new InstaPostManagementException("SERVICE.COMMENT_NOT_FOUND"));

		if (!comment.getUserId().equals(userId)) {
			throw new InstaPostManagementException("SERVICE.UNAUTHORIZED_COMMENT_DELETE");
		}

		if (!comment.getPost().getPostId().equals(postId)) {
			throw new InstaPostManagementException("SERVICE.COMMENT_NOT_ON_POST");
		}

		commentRepository.delete(comment);
	}
}
