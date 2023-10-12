package com.example.vital_hub.exercises.data_container;

public class SingleExercise {

    private Long id;

    private Long groupId;

    private String name;

    private String description;

    private Float totalCalo;

    private Integer sets;

    private Integer repsPerSet;

    public SingleExercise() {

    }

    public SingleExercise(Long id,
                          Long groupId,
                          String name,
                          String description,
                          Float totalCalo,
                          Integer sets,
                          Integer repsPerSet) {
        this.id = id;
        this.groupId = groupId;
        this.name = name;
        this.description = description;
        this.totalCalo = totalCalo;
        this.sets = sets;
        this.repsPerSet = repsPerSet;
    }
}
