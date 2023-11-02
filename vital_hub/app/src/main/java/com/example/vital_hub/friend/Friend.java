package com.example.vital_hub.friend;

import java.io.Serializable;

public class Friend implements Serializable {
    private final Long id;
    private final String name;
    private final String avatar;
    private final String status;

    public Friend(Long id, String name, String avatar, String status) {
        this.id = id;
        this.name = name;
        this.avatar = avatar;
        this.status = status;
    }

    public Long getId() { return id; }
    public String getName() { return name; }

    public String getAvatar() { return avatar; }

    public String getStatus() {
        switch (status) {
            case "FRIEND":
                return "Friend";
            case "PENDING":
                return "Pending Request";
            case "INCOMING":
                return "Incoming Request";
            default:
                return "Unknown";
        }
    }

    public String getRawStatus() {
        return status;
    }
}
