package com.main.server.service;

import com.main.server.entity.Post;
import com.main.server.utils.dto.PostDto;
import com.main.server.utils.enums.ExerciseType;

import java.util.List;

public interface PostService {
    List<PostDto> postByPage(int page, int pageSize, Long userId);

    PostDto postById(Long id, Long userId, ExerciseType type);

    Post deletePost(Long id);
}
