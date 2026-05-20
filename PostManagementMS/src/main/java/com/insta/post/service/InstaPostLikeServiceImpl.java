package com.insta.post.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.insta.post.dto.LikeDTO;
import com.insta.post.entity.Like;
import com.insta.post.entity.Post;
import com.insta.post.exception.InstaPostManagementException;
import com.insta.post.repository.LikeRepository;
import com.insta.post.repository.PostManagementRepository;

@Service("instaPostLikeService")
public class InstaPostLikeServiceImpl implements InstaPostLikeService {

	@Autowired
	PostManagementRepository postManagementRepository;
	@Autowired
	ModelMapper modelMapper;
	@Autowired
	LikeRepository likeRepository;

	@Override
	public Long likePost(Long userId, Long postId) throws InstaPostManagementException {
		if (likeRepository.existsByUserIdAndPost_PostId(userId, postId)) {
			throw new InstaPostManagementException("SERVICE.LIKE_ALREADY_EXISTS");
		}
		Like l = new Like();
		Post post = postManagementRepository.findById(postId)
				.orElseThrow(() -> new InstaPostManagementException("SERVICE.POST_NOT_FOUND"));
		l.setPost(post);
		l.setUserId(userId);
		l.setCreatedAt(LocalDateTime.now());
		return likeRepository.save(l).getLikeId();
	}

	@Override
	public Long unlikePost(Long userId, Long postId) throws InstaPostManagementException {
		Like like = likeRepository.findFirstByUserIdAndPost_PostId(userId, postId)
				.orElseThrow(() -> new InstaPostManagementException("SERVICE.LIKE_NOT_FOUND"));
		likeRepository.delete(like);
		return like.getLikeId();
	}

	@Override
	public List<LikeDTO> getAllLikeByPostId(Long postId) throws InstaPostManagementException {
		List<Like> l = likeRepository.findByPost_PostId(postId);
		if (l.isEmpty())
			throw new InstaPostManagementException("SERVICE.LIKE_NOT_FOUND");
		List<LikeDTO> nl = new ArrayList<>();
		l.forEach((item) -> {
			LikeDTO n = new LikeDTO();
			n.setLikeId(item.getLikeId());
			n.setPostId(item.getPost().getPostId());
			n.setUserId(item.getUserId());
			n.setCreatedAt(item.getCreatedAt());
			nl.add(n);
		});
		return nl;
	}
	
	@Override
	public Long countLikesByPostId(Long postId) throws InstaPostManagementException {
		return likeRepository.countLikesByPostId(postId);
	}

	@Override
	public boolean isPostLikedByUser(Long userId, Long postId) {
		if (userId == null) return false;
		return likeRepository.existsByUserIdAndPost_PostId(userId, postId);
	}

}
