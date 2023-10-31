package com.example.vital_hub.profile;

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

    public int getPosition() {
        if (name == "Female") {
            return 0;
        } else {
            return 1;
        }
    }
}
