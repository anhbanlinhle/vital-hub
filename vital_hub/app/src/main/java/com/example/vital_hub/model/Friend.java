package com.example.vital_hub.model;

public class Friend {
    private String name;
    private String avatar;

    public Friend(String name, String avatar) {
        this.name = name;
        this.avatar = avatar;
    }

    public String getName() { return name; }

    public String getAvatar() { return avatar; }
}
