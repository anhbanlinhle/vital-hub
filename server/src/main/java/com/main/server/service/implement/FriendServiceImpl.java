package com.main.server.service.implement;

import com.main.server.repository.FriendRepository;
import com.main.server.service.FriendService;
import com.main.server.utils.dto.UserDto;
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
    public List<UserDto> getFriendList(Long id, String name, Integer limit, Integer offset) {

        return friendRepository.getFriendList(id, name, limit, offset);
    }

    @Override
    public List<UserDto> getRequestFriendList(Long id, Integer limit, Integer offset) {

        return friendRepository.getRequestFriendList(id, limit, offset);
    }

    @Override
    public List<UserDto> searchFriend(Long id, String name, Integer limit, Integer offset) {

        return friendRepository.searchNonFriendList(id, name, limit, offset);
    }

    @Override
    public void addFriend(Long id, Long friendId) {
        friendRepository.addFriend(id, friendId);
    }

    @Override
    public void deleteFriend(Long id, Long friendId) {
        friendRepository.deleteFriend(id, friendId);
    }

    @Override
    public void acceptFriend(Long id, Long friendId) {
        friendRepository.acceptFriend(id, friendId);
    }

    @Override
    public void denyFriend(Long id, Long friendId) {
        friendRepository.denyFriend(id, friendId);
    }

    @Override
    public void revokeRequest(Long id, Long friendId) {
        friendRepository.revokeRequest(id, friendId);
    }


}
