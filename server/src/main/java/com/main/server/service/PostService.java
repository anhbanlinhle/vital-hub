package com.main.server.service;

import com.main.server.entity.Post;
import com.main.server.utils.dto.PostDto;

import java.util.List;

public interface PostService {
    List<PostDto> postByPage(int page, int pageSize, Long userId);

    Post postById(Long id);
}
