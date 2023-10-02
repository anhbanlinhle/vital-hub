package com.main.server.controller;

import com.main.server.entity.User;
import com.main.server.middleware.CamelCaseMiddleware;
import com.main.server.request.CheckObjRequest;
import com.main.server.utils.enums.Sex;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping("/test/single-user")
    public ResponseEntity<?> test1() {
        User user = new User();
        user.setId(100L);
        user.setUserDetail(null);
        user.setSex(Sex.MALE);
        user.setName("tuan dep trai");
        user.setAvatar("111111111111");
        user.setPhoneNo("0192401124");
        user.setGmail("tuan@123");
        return ResponseEntity.ok().body(user);
    }

    @GetMapping("/test/list-user")
    public ResponseEntity<?> test2() {
        List<User> users = new ArrayList<>();

        User user = new User();
        user.setId(100L);
        user.setUserDetail(null);
        user.setSex(Sex.MALE);
        user.setName("tuan dep trai");
        user.setAvatar("111111111111");
        user.setPhoneNo("0192401124");
        user.setGmail("tuan@123");
        users.add(user);

        User user2 = new User();
        user2.setId(102L);
        user2.setUserDetail(null);
        user2.setSex(Sex.MALE);
        user2.setName("tuan dep trai");
        user2.setAvatar("111111111111");
        user2.setPhoneNo("0192401124");
        user2.setGmail("tuan@123");
        users.add(user2);

        User user3 = new User();
        user3.setId(103L);
        user3.setUserDetail(null);
        user3.setSex(Sex.MALE);
        user3.setName("tuan dep trai");
        user3.setAvatar("111111111111");
        user3.setPhoneNo("0192401124");
        user3.setGmail("tuan@123");
        users.add(user3);

        return ResponseEntity.ok().body(users);
    }
}
