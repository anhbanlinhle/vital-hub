package com.main.server.utils.enums;

public enum ExerciseType {
    RUNNING("Running"),
    BICYCLING("Bicycling"),
    PUSHUP("Push up"),
    GYM("Gym");
    private String name;

    ExerciseType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
