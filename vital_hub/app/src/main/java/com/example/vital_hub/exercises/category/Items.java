package com.example.vital_hub.exercises.category;

public class Items {
    private static long counter = 0L;
    private long id;
    private String name;
    private String description;

    public Items(String name, String description) {
        this.id = counter++;
        this.name = name;
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
