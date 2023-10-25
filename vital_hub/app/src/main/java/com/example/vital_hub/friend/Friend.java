package com.example.vital_hub.friend;

import java.io.Serializable;

public class Friend implements Serializable {
    private final Long id;
    private final String name;
    private final String avatar;

    public Friend(Long id, String name, String avatar) {
        this.id = id;
        this.name = name;
        this.avatar = avatar;
    }

    public Long getId() { return id; }
    public String getName() { return name; }

    public String getAvatar() { return avatar; }
}
