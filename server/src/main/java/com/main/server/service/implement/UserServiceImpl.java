package com.main.server.service.implement;

import com.main.server.entity.User;
import com.main.server.entity.UserDetail;
import com.main.server.repository.UserDetailRepository;
import com.main.server.repository.UserRepository;
import com.main.server.request.UserInfoRequest;
import com.main.server.service.UserService;
import com.main.server.utils.dto.FirstSignDto;
import com.main.server.utils.dto.UserDto;
import com.main.server.utils.enums.Sex;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final UserDetailRepository userDetailRepository;

    @Override
    public void createUser(UserInfoRequest userInfoRequest) {
        
        User checkUser = userRepository.findByGmail(userInfoRequest.getGmail()).orElse(null);

        if (checkUser != null) {
            throw new RuntimeException("User already exists");
        }

        User user = User.builder()
                .gmail(userInfoRequest.getGmail())
                .name(userInfoRequest.getName())
                .sex(userInfoRequest.getSex())
                .phoneNo(userInfoRequest.getPhoneNo())
                .avatar(userInfoRequest.getAvatar())
                .dob(LocalDate.parse(userInfoRequest.getDob()))
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
                .dob(LocalDate.parse(userInfoRequest.getDob()))
                .build();

        userRepository.save(user);
    }

    @Override
    public void createUser(FirstSignDto firstSignDto) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        User user = userRepository.save(User.builder()
                .id(null)
                .gmail(firstSignDto.getGmail())
                .sex(Sex.valueOf(firstSignDto.getSex()))
                .dob(LocalDate.parse(firstSignDto.getDob(), formatter))
                .phoneNo(firstSignDto.getPhoneNo())
                .avatar(firstSignDto.getAvatar())
                .name(firstSignDto.getName())
                .build());

        userDetailRepository.save(UserDetail.builder()
                .userId(user.getId())
                .currentHeight(firstSignDto.getCurrentHeight())
                .currentWeight(firstSignDto.getCurrentWeight())
                .description(firstSignDto.getDescription())
                .exerciseDaysPerWeek(firstSignDto.getExerciseDaysPerWeek())
                .build()
        );
    }

    @Override
    public User getUserDetailById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public UserDto getUserDtoById(Long id) {
        return userRepository.findUserDtoById(id);
    }
}
