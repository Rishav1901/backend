package com.infy.instagram.trendingposts.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.infy.instagram.trendingposts.dto.TrendingHashtagResponseDto;
import com.infy.instagram.trendingposts.dto.TrendingPostResponseDto;
import com.infy.instagram.trendingposts.entity.TrendingHashtag;
import com.infy.instagram.trendingposts.entity.TrendingPost;

@Component
public class TrendingMapper {

    private final ModelMapper modelMapper;

    public TrendingMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public TrendingPostResponseDto toTrendingPostResponseDto(TrendingPost entity) {
        return modelMapper.map(entity, TrendingPostResponseDto.class);
    }

    public TrendingHashtagResponseDto toTrendingHashtagResponseDto(TrendingHashtag entity) {
        return modelMapper.map(entity, TrendingHashtagResponseDto.class);
    }
}