package com.main.server.request;

import com.main.server.entity.UserDetail;
import com.main.server.utils.enums.Sex;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailRequest {
    private String gmail;
    private String name;
    private Sex sex;
    private String phoneNo;
    private String avatar;
    private String dob;
    private UserDetail userDetail;
}
