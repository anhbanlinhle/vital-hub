package com.example.vital_hub.profile;

public class UserInfo {
    private Long id;
    private String name;
    private String avatar;

    public UserInfo(Long id, String name, String avatar) {
        this.id = id;
        this.name = name;
        this.avatar = avatar;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAvatar() {
        return avatar;
    }
}
