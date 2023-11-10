package com.example.vital_hub.profile;

public class UserInfo {
    private Long id;
    private String name;
    private String avatar;
    private String status;

    public UserInfo(Long id, String name, String avatar, String status) {
        this.id = id;
        this.name = name;
        this.avatar = avatar;
        this.status = status;
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
    public String getStatus() {
        return status;
    }
}
