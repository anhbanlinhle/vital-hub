package com.main.server.service;

import com.main.server.entity.Post;

import java.util.List;

public interface PostService {
    List<Post> postByPage(int page, int pageSize, Long userId);

    Post postById(Long id);
}
