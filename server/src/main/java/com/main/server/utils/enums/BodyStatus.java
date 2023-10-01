package com.main.server.utils.enums;

public enum BodyStatus {
    OBESITY("Obesity"),
    FAT("FAT"),
    NORMAL("Normal"),
    THIN("Thin"),
    SUPER_THIN("Super thin");
    private String name;

    BodyStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
