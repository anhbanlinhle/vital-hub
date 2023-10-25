package com.main.server.service;

import com.main.server.entity.User;
import com.main.server.request.UserInfoRequest;
import com.main.server.utils.dto.FirstSignDto;
import com.main.server.utils.dto.UserDto;

public interface UserService {
    public void createUser(UserInfoRequest userInfoRequest);

    public void updateUser(UserInfoRequest userInfoRequest);

    void createUser(FirstSignDto firstSignDto);

    User getUserDetailById(Long id);

    UserDto getUserDtoById(Long id);
}
