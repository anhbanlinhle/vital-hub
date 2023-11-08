package com.main.server.service.implement;

import com.main.server.entity.Post;
import com.main.server.repository.FriendRepository;
import com.main.server.repository.PostRepository;
import com.main.server.service.PostService;
import com.main.server.utils.dto.PostDto;
import com.main.server.utils.enums.ExerciseType;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
        friendList.add(userId);
        List<PostDto> posts = postRepository.allPostOrderByCreatedTime(page * pageSize, pageSize, friendList, userId);
        for (int i = 0; i < posts.size(); i++) {
            if (posts.get(i).getPostId() == null) {
                posts.remove(i--);
            }
        }
        return posts;
    }

    @Override
    public PostDto postById(Long id, Long userId, ExerciseType type) {
        PostDto post;
        if (type == ExerciseType.BICYCLING) {
            post = postRepository.getPostBicyclingWithUserByPid(id, userId).orElse(null);
        } else if (type == ExerciseType.RUNNING) {
            post = postRepository.getPostRunningWithUserByPid(id, userId).orElse(null);
        } else if (type == ExerciseType.PUSHUP) {
            post = postRepository.getPostPushUpWithUserByPid(id, userId).orElse(null);
        } else {
            post = postRepository.getPostGymWithUserByPid(id, userId).orElse(null);
        }
        return post;
    }

    @Override
    public Post deletePost(Long id) {
        Post post = postRepository.findByIdAndIsDeletedFalse(id).orElse(null);
        if (post == null) {
            return null;
        } else {
            post.setIsDeleted(true);
            postRepository.save(post);
            return post;
        }
    }
}
