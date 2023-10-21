package com.example.vital_hub.friend;

import java.io.Serializable;

public class Friend implements Serializable {
    private String name;
    private String avatar;

    public Friend(String name, String avatar) {
        this.name = name;
        this.avatar = avatar;
    }

    public String getName() { return name; }

    public String getAvatar() { return avatar; }
}
