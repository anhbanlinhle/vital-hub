package com.main.server.service.implement;

import com.main.server.entity.Friend;
import com.main.server.entity.User;
import com.main.server.repository.FriendRepository;
import com.main.server.service.FriendService;
import com.main.server.utils.dto.FriendListDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService {

    @Autowired
    private final FriendRepository friendRepository;

    @Override
    public int countFriend(Long id) {

        return friendRepository.countFriend(id);
    }

    @Override
    public List<FriendListDto> getFriendList(Long id, Integer limit, Integer offset) {

        return friendRepository.getFriendList(id, limit, offset);
    }
}
