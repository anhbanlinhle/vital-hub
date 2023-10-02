package com.main.server.utils.enums;

public enum Sex {

    MALE("Male"),
    FEMALE("Female");
    private String name;

    Sex(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
