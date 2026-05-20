package com.insta.post.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.insta.post.dto.ViewDTO;
import com.insta.post.entity.Post;
import com.insta.post.entity.View;
import com.insta.post.exception.InstaPostManagementException;
import com.insta.post.repository.PostManagementRepository;
import com.insta.post.repository.ViewRepository;

@Service("instaPostViewService")
public class InstaPostViewServiceImpl implements InstaPostViewService {

	@Autowired
	ModelMapper modelMapper;
	@Autowired
	ViewRepository viewRepository;
	@Autowired
	PostManagementRepository postManagementRepository;

	@Override
	public Long viewPost(Long userId, Long postId) throws InstaPostManagementException {
		View v = new View();
		Post post = postManagementRepository.findById(postId)
				.orElseThrow(() -> new InstaPostManagementException("SERVICE.POST_NOT_FOUND"));
		v.setPost(post);
		v.setUserId(userId);
		v.setViewedAt(LocalDateTime.now());
		return viewRepository.save(v).getPostViewId();
	}

	@Override
	public List<ViewDTO> getAllViewById(Long postId) throws InstaPostManagementException {
		List<View> l = viewRepository.findByPost_PostId(postId);
		if (l.isEmpty())
			throw new InstaPostManagementException("SERVICE.LIKES_NOT_FOUND");
		List<ViewDTO> nl = new ArrayList<>();
		l.forEach(c -> {
			ViewDTO v = new ViewDTO();
			v.setPostViewId(c.getPostViewId());
			v.setUserId(c.getUserId());
			v.setViewedAt(c.getViewedAt());
			v.setPostId(c.getPost().getPostId());
			nl.add(v);
		});
		return nl;
	}
	
	@Override
	public Long countViewsByPostId(Long postId) {
		return viewRepository.countViewsByPostId(postId);
	}

}
