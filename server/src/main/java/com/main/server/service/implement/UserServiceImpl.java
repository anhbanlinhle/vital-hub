package com.main.server.service.implement;

import com.main.server.entity.User;
import com.main.server.repository.UserRepository;
import com.main.server.request.UserInfoRequest;
import com.main.server.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private final UserRepository userRepository;

    @Override
    public void createUser(UserInfoRequest userInfoRequest) {

        if (userRepository.findByGmail(userInfoRequest.getGmail()) != null) {
            throw new RuntimeException("User already exists");
        }

        User user = User.builder()
                .gmail(userInfoRequest.getGmail())
                .name(userInfoRequest.getName())
                .sex(userInfoRequest.getSex())
                .phoneNo(userInfoRequest.getPhoneNo())
                .avatar(userInfoRequest.getAvatar())
                .dob(Date.valueOf(userInfoRequest.getDob()))
                .build();

        userRepository.save(user);
    }

    @Override
    public void updateUser(UserInfoRequest userInfoRequest) {



        User user = User.builder()
                .id(userRepository.findIdByGmail(userInfoRequest.getGmail()))
                .gmail(userInfoRequest.getGmail())
                .name(userInfoRequest.getName())
                .sex(userInfoRequest.getSex())
                .phoneNo(userInfoRequest.getPhoneNo())
                .avatar(userInfoRequest.getAvatar())
                .dob(Date.valueOf(userInfoRequest.getDob()))
                .build();

        userRepository.save(user);
    }
}
