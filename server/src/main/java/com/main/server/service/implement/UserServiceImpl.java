package com.main.server.service.implement;

import com.main.server.entity.User;
import com.main.server.entity.UserDetail;
import com.main.server.repository.UserDetailRepository;
import com.main.server.repository.UserRepository;
import com.main.server.request.UserDetailRequest;
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
import java.util.List;
import java.util.Optional;

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
    public UserDto getUserDtoById(Long self_id, Long other_id) {
        return userRepository.findUserDtoById(self_id, other_id);
    }

    @Override
    public List<UserDto> findUser(Long self_id, String name, Integer limit, Integer offset) {
        return userRepository.findUser(self_id, name, limit, offset);
    }

    @Override
    public void saveUserDetail(Long selfId, UserDetailRequest request) {
        Long detailId = userDetailRepository.findIdByUserId(selfId);
        UserDetail userDetail = UserDetail.builder()
                .id(detailId)
                .userId(selfId)
                .currentHeight(request.getUserDetail().getCurrentHeight())
                .currentWeight(request.getUserDetail().getCurrentWeight())
                .description(request.getUserDetail().getDescription())
                .exerciseDaysPerWeek(request.getUserDetail().getExerciseDaysPerWeek())
                .build();

        User user = User.builder()
                .id(selfId)
                .gmail(request.getGmail())
                .name(request.getName())
                .sex(request.getSex())
                .avatar(request.getAvatar())
                .phoneNo(request.getPhoneNo())
                .dob(Date.valueOf(request.getDob()).toLocalDate())
                .build();
        userRepository.save(user);
        userDetailRepository.save(userDetail);
    }
}
