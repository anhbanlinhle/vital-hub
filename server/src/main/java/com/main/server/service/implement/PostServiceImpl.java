package com.main.server.service.implement;

import com.main.server.entity.Post;
import com.main.server.middleware.TokenParser;
import com.main.server.repository.FriendRepository;
import com.main.server.repository.PostRepository;
import com.main.server.service.PostService;
import com.main.server.utils.dto.PostDto;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class PostServiceImpl implements PostService {

    @Autowired
    final FriendRepository friendRepository;

    @Autowired
    final PostRepository postRepository;



    @Override
    public List<PostDto> postByPage(int page, int pageSize, Long userId) {
        List<Long> friendList = friendRepository.findFriendList(userId);
        List<PostDto> posts = postRepository.allPostOrderByCreatedTime(page * pageSize, pageSize, friendList, friendList.isEmpty());
        return posts;
    }

    @Override
    public Post postById(Long id) {
        Post post = postRepository.findByIdAndIsDeletedFalse(id).orElse(null);
        return post;
    }
}
