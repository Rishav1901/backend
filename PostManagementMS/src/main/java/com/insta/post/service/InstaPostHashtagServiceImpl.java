package com.insta.post.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.insta.post.dto.HashtagDTO;
import com.insta.post.entity.Hashtag;
import com.insta.post.entity.Post;
import com.insta.post.entity.PostHashtag;
import com.insta.post.exception.InstaPostManagementException;
import com.insta.post.repository.HashTagRepository;
import com.insta.post.repository.PostHashtagRepository;
import com.insta.post.repository.PostManagementRepository;

@Service("instaPostHashtagService")
public class InstaPostHashtagServiceImpl implements InstaPostHashtagService {

	@Autowired
	ModelMapper modelMapper;
	@Autowired
	HashTagRepository hashTagRepository;
	@Autowired
	PostHashtagRepository postHashtagRepository;
	@Autowired
	PostManagementRepository postManagementRepository;

	@Override
	public Hashtag createHashtag(String tag) throws InstaPostManagementException {
		Hashtag h = new Hashtag();
		h.setTag(tag);
		h.setCreatedAt(LocalDateTime.now());
		return hashTagRepository.save(h);
	}

	public HashtagDTO findHashtagById(Long hashtagId) throws InstaPostManagementException {
		Optional<Hashtag> optional = hashTagRepository.findById(hashtagId);
		Hashtag h = optional.orElseThrow(() -> new InstaPostManagementException("SERVICE.TAG_NOT_FOUND"));
		return modelMapper.map(h, HashtagDTO.class);
	}

	public HashtagDTO findHashtagByTag(String tag) throws InstaPostManagementException {
		Optional<Hashtag> optional = hashTagRepository.findByTag(tag);
		Hashtag h = optional.orElseThrow(() -> new InstaPostManagementException("SERVICE.TAG_NOT_FOUND"));
		return modelMapper.map(h, HashtagDTO.class);
	}

	@Override
	public Long postHashtag(Long postId, String tagName) throws InstaPostManagementException {
		Post post = postManagementRepository.findById(postId)
				.orElseThrow(() -> new InstaPostManagementException("SERVICE.POST_NOT_FOUND"));
		Optional<Hashtag> optional = hashTagRepository.findByTag(tagName);
		Hashtag tag;
		if (optional.isEmpty()) {
			tag = createHashtag(tagName);
			tag.setUsageCount(1L);
		} else {
			tag = optional.get();
			tag.setUsageCount(tag.getUsageCount()==null?1L:tag.getUsageCount()+1L);
		}
		PostHashtag h = new PostHashtag();
		h.setHashtag(tag);
		h.setPost(post);
		return postHashtagRepository.save(h).getPostHashtagId();
	}

	@Override
	public List<String> getPostHashtag(Long postId) throws InstaPostManagementException {
		List<PostHashtag> l = postHashtagRepository.findByPost_PostId(postId);
		List<String> n= new ArrayList<>();
		l.forEach(item->{
			n.add(item.getHashtag().getTag());
		});
		return n;
	}
	
	@Override
	public List<HashtagDTO> findAllHashtags() throws InstaPostManagementException{
		List<Hashtag> hashtagList = hashTagRepository.findAll();
		return hashtagList.stream().map(s-> modelMapper.map(s, HashtagDTO.class)).toList();
	}
	
	@Override
	public List<HashtagDTO> findByQuerytag(String tag) throws InstaPostManagementException{
		return hashTagRepository
				.findByQuerytag("#"+tag)
				.stream()
				.map(h -> modelMapper.map(h, HashtagDTO.class))
				.toList();
	}
	
	@Override
	public List<Long> findDistinctPostIdsByHashtagId(Long hashtagId) throws InstaPostManagementException{
		return postHashtagRepository.findDistinctPostIdsByHashtagId(hashtagId);
	}
	
	
	@Override
	public List<HashtagDTO> findByTagIdList(List<Long> list) throws InstaPostManagementException{
		List<HashtagDTO> result = new ArrayList<>();
		for(Long id: list) {
			result.add(findHashtagById(id));
		}
		return result;
	}

	@Override
	public List<String> extractHashtags(String text) throws InstaPostManagementException {
		List<String> tags = new ArrayList<>();
		Pattern pattern = Pattern.compile("#\\w+");
		Matcher matcher = pattern.matcher(text);
		while (matcher.find()) {
			tags.add(matcher.group());
		}
		return tags;
	}

	@Override
	public void deletePostHashtags(Long postId) throws InstaPostManagementException {
		List<PostHashtag> postHashtags = postHashtagRepository.findByPost_PostId(postId);
		postHashtagRepository.deleteAll(postHashtags);
	}

	@Override
	public List<Long> extractMentionedUserIds(String text) throws InstaPostManagementException {
		List<Long> mentionedIds = new ArrayList<>();
		Pattern pattern = Pattern.compile("@(\\w+)");
		Matcher matcher = pattern.matcher(text);
		while (matcher.find()) {
			String username = matcher.group(1);
			// Note: This would require service-to-service call to auth service to resolve username to userId
			// For now, returning empty - implementation would fetch userId from username
		}
		return mentionedIds;
	}

}
