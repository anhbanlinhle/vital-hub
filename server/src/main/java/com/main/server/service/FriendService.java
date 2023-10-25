package com.main.server.service;

import com.main.server.utils.dto.UserDto;

import java.util.List;

public interface FriendService {
    int countFriend(Long id);
    List<UserDto> getFriendList(Long id, String name, Integer limit, Integer offset);

    void addFriend(Long id, Long friendId);

    void deleteFriend(Long id, Long friendId);

    void acceptFriend(Long id, Long friendId);
}
