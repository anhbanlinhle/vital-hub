package com.main.server.service.implement;

import com.main.server.entity.Post;
import com.main.server.middleware.TokenParser;
import com.main.server.repository.FriendRepository;
import com.main.server.repository.PostRepository;
import com.main.server.service.PostService;
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
    public List<Post> postByPage(int page, int pageSize, Long userId) {
        List<Long> friendList = friendRepository.findFriendList(userId);
        Page<Post> posts = postRepository.allPostOrderByCreatedTime(PageRequest.of(page, pageSize), friendList, friendList.isEmpty());
        return posts.getContent();
    }
}
