package com.example.vital_hub.exercises.data_container;

public class GroupExercise {
    private Long groupId;
    private Integer exerciseCount;
    private Float totalCalo;

    public GroupExercise() {

    }

    public GroupExercise(Long groupId,
                         Integer exerciseCount,
                         Float totalCalo) {
        this.groupId = groupId;
        this.exerciseCount = exerciseCount;
        this.totalCalo = totalCalo;
    }

    public String getTotalCaloStr() {
        return "Total " + this.totalCalo + " calories";
    }

    public String getGroupName() {
        return "Group " + this.groupId;
    }

    public String getExerciseCountStr() {
        return "Number of exercises: " + this.exerciseCount;
    }
}
