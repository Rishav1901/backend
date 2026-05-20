package com.insta.post.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.insta.post.dto.SavePostDTO;
import com.insta.post.entity.Post;
import com.insta.post.entity.SavedPost;
import com.insta.post.exception.InstaPostManagementException;
import com.insta.post.repository.PostManagementRepository;
import com.insta.post.repository.SavedPostRepository;

@Service("instaPostSavedService")
public class InstaPostSavedServiceImpl implements InstaPostSavedService {
	@Autowired
	PostManagementRepository postManagementRepository;
	@Autowired
	ModelMapper modelMapper;
	@Autowired
	SavedPostRepository savedPostRepository;

	@Override
	public Long createSavePost(Long userId, Long postId) throws InstaPostManagementException {
		java.util.Optional<SavedPost> existing = savedPostRepository.findFirstByUserIdAndPost_PostId(userId, postId);
		if (existing.isPresent()) {
			return existing.get().getSavedPostId();
		}
		SavedPost s = new SavedPost();
		Post post = postManagementRepository.findById(postId)
				.orElseThrow(() -> new InstaPostManagementException("SERVICE.POST_NOT_FOUND"));
		s.setUserId(userId);
		s.setCreatedAt(LocalDateTime.now());
		s.setPost(post);
		return savedPostRepository.save(s).getSavedPostId();
	}

	@Override
	public List<SavePostDTO> getAllSavePost(Long postId) throws InstaPostManagementException {
		List<SavedPost> l = savedPostRepository.findByPost_PostId(postId);
		if (l.isEmpty())
			throw new InstaPostManagementException("SERVICE.SAVED_NOT_FOUND");
		List<SavePostDTO> nl = new ArrayList<>();
		l.forEach((item) -> {
			SavePostDTO n = new SavePostDTO();
			n.setPostId(item.getPost().getPostId());
			n.setSavedPostId(item.getSavedPostId());
			n.setUserId(item.getUserId());
			nl.add(n);
		});
		return nl;

	}
	
	@Override
	public Long countSavesByPostId(Long PostId) {
		return savedPostRepository.countSavesByPostId(PostId);
	}

	@Override
	public Long deleteSavePost(Long userId, Long postId) throws InstaPostManagementException {
		SavedPost savedPost = savedPostRepository.findFirstByUserIdAndPost_PostId(userId, postId)
				.orElseThrow(() -> new InstaPostManagementException("SERVICE.SAVED_NOT_FOUND"));
		savedPostRepository.delete(savedPost);
		return savedPost.getSavedPostId();
	}

	@Override
	public boolean isPostSavedByUser(Long userId, Long postId) {
		if (userId == null) return false;
		return savedPostRepository.findFirstByUserIdAndPost_PostId(userId, postId).isPresent();
	}

}
