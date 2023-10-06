package com.main.server.service;

import com.main.server.request.UserInfoRequest;

public interface UserService {
    public void createUser(UserInfoRequest userInfoRequest);

    public void updateUser(UserInfoRequest userInfoRequest);
}
