package com.example.vital_hub.profile;

public class UserDetail {
    private Long id;
    private String name;
    private String gmail;
    private Sex sex;
    private String phoneNo;
    private String avatar;
    private String dob;
    private DetailedProfile userDetail;
    public UserDetail(Long id, String name, String gmail, Sex sex, String phoneNo, String avatar, String dob, DetailedProfile userDetail) {
        this.id = id;
        this.name = name;
        this.gmail = gmail;
        this.sex = sex;
        this.phoneNo = phoneNo;
        this.avatar = avatar;
        this.dob = dob;
        this.userDetail = userDetail;
    }

    public UserDetail(String name, String gmail, String phoneNo, String dob, DetailedProfile userDetail) {
        this.name = name;
        this.gmail = gmail;
        this.phoneNo = phoneNo;
        this.dob = dob;
        this.userDetail = userDetail;
    }

    public Long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getGmail() {
        return gmail;
    }
    public Sex getSex() {
        return sex;
    }
    public String getPhoneNo() {
        return phoneNo;
    }
    public String getAvatar() {
        return avatar;
    }
    public String getDob() {
        return dob;
    }
    public DetailedProfile getUserDetail() {
        return userDetail;
    }
}
