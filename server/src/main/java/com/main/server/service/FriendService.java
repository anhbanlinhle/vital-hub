package com.main.server.service;

import com.main.server.entity.Friend;
import com.main.server.entity.User;
import com.main.server.utils.dto.FriendListDto;

import java.util.List;

public interface FriendService {
    public int countFriend(Long id);
    public List<FriendListDto> getFriendList(Long id, String name, Integer limit, Integer offset);
}
