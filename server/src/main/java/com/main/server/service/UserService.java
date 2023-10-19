package com.main.server.service;

import com.main.server.request.UserInfoRequest;
import com.main.server.utils.dto.FirstSignDto;

public interface UserService {
    public void createUser(UserInfoRequest userInfoRequest);

    public void updateUser(UserInfoRequest userInfoRequest);

    void createUser(FirstSignDto firstSignDto);
}
